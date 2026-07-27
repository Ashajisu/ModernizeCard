package com.test.zoom.service.journal;

import com.test.zoom.dto.journal.AccountBalanceDto;
import com.test.zoom.dto.journal.BalanceSheetResponse;
import com.test.zoom.entity.journal.Account;
import com.test.zoom.repository.journal.AccountRepository;
import com.test.zoom.repository.journal.JournalLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 대차대조표 산출.
 * - ASSET: 잔액 = 차변합 - 대변합
 * - LIABILITY: 잔액 = 대변합 - 차변합
 * - 상위(그룹) 계정은 하위 계정 잔액을 재귀적으로 합산(rollup)한다.
 * - 자본(Equity)은 별도 계정 없이 "자산 - 부채"로 자동 계산(플러그)한다.
 */
@Service
@RequiredArgsConstructor
public class BalanceSheetService {

    private final AccountRepository accountRepository;
    private final JournalLineRepository journalLineRepository;

    public BalanceSheetResponse generate(LocalDate asOfDate) {
        List<Account> allAccounts = accountRepository.findAll();

        // accountId -> [confirmedNetDebitCredit, unconfirmedNetDebitCredit] 원본 라인 집계 (본인 계정 직접분만, 하위 rollup 전)
        Map<Long, BigDecimal[]> ownConfirmed = new HashMap<>();   // [debit, credit]
        Map<Long, BigDecimal[]> ownUnconfirmed = new HashMap<>();

        for (Object[] row : journalLineRepository.aggregateBalancesAsOf(asOfDate)) {
            Long accountId = (Long) row[0];
            Boolean confirmed = (Boolean) row[1];
            BigDecimal debit = (BigDecimal) row[2];
            BigDecimal credit = (BigDecimal) row[3];
            Map<Long, BigDecimal[]> target = Boolean.TRUE.equals(confirmed) ? ownConfirmed : ownUnconfirmed;
            target.put(accountId, new BigDecimal[]{debit, credit});
        }

        Map<Long, Account> byId = allAccounts.stream().collect(Collectors.toMap(Account::getId, a -> a));
        Map<Long, List<Account>> childrenOf = new HashMap<>();
        for (Account a : allAccounts) {
            Long parentId = a.getParent() != null ? a.getParent().getId() : null;
            childrenOf.computeIfAbsent(parentId, k -> new ArrayList<>()).add(a);
        }

        List<AccountBalanceDto> assets = new ArrayList<>();
        List<AccountBalanceDto> liabilities = new ArrayList<>();

        List<Account> assetRoots = childrenOf.getOrDefault(null, List.of()).stream()
                .filter(a -> a.getCategory() == Account.AccountCategory.ASSET)
                .sorted(Comparator.comparing(Account::getSortOrder))
                .toList();
        List<Account> liabilityRoots = childrenOf.getOrDefault(null, List.of()).stream()
                .filter(a -> a.getCategory() == Account.AccountCategory.LIABILITY)
                .sorted(Comparator.comparing(Account::getSortOrder))
                .toList();

        BigDecimal totalAssets = BigDecimal.ZERO;
        BigDecimal totalAssetsConfirmed = BigDecimal.ZERO;
        for (Account root : assetRoots) {
            BigDecimal[] result = rollup(root, childrenOf, ownConfirmed, ownUnconfirmed, true, 0, assets);
            totalAssets = totalAssets.add(result[0]);
            totalAssetsConfirmed = totalAssetsConfirmed.add(result[1]);
        }

        BigDecimal totalLiabilities = BigDecimal.ZERO;
        BigDecimal totalLiabilitiesConfirmed = BigDecimal.ZERO;
        for (Account root : liabilityRoots) {
            BigDecimal[] result = rollup(root, childrenOf, ownConfirmed, ownUnconfirmed, false, 0, liabilities);
            totalLiabilities = totalLiabilities.add(result[0]);
            totalLiabilitiesConfirmed = totalLiabilitiesConfirmed.add(result[1]);
        }

        BigDecimal equity = totalAssets.subtract(totalLiabilities);
        BigDecimal equityConfirmed = totalAssetsConfirmed.subtract(totalLiabilitiesConfirmed);

        BalanceSheetResponse response = new BalanceSheetResponse();
        response.setAsOfDate(asOfDate);
        response.setAssets(assets);
        response.setLiabilities(liabilities);
        response.setTotalAssets(totalAssets);
        response.setTotalLiabilities(totalLiabilities);
        response.setEquity(equity);
        response.setTotalAssetsConfirmedOnly(totalAssetsConfirmed);
        response.setTotalLiabilitiesConfirmedOnly(totalLiabilitiesConfirmed);
        response.setEquityConfirmedOnly(equityConfirmed);
        response.setBalanced(true); // 자본을 플러그로 계산하므로 항상 성립
        return response;
    }

    /**
     * 재귀적으로 하위 계정 잔액을 합산하며 리스트에 순서대로 추가(부모 먼저, 자식 들여쓰기).
     * 반환값: [totalAmount(확정+미확정), confirmedOnlyAmount]
     */
    private BigDecimal[] rollup(Account account, Map<Long, List<Account>> childrenOf,
                                 Map<Long, BigDecimal[]> ownConfirmed, Map<Long, BigDecimal[]> ownUnconfirmed,
                                 boolean isAsset, int depth, List<AccountBalanceDto> out) {

        List<Account> children = childrenOf.getOrDefault(account.getId(), List.of()).stream()
                .sorted(Comparator.comparing(Account::getSortOrder)).toList();

        BigDecimal[] confirmedDC = ownConfirmed.getOrDefault(account.getId(), new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});
        BigDecimal[] unconfirmedDC = ownUnconfirmed.getOrDefault(account.getId(), new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});

        BigDecimal ownConfirmedNet = isAsset
                ? confirmedDC[0].subtract(confirmedDC[1])
                : confirmedDC[1].subtract(confirmedDC[0]);
        BigDecimal ownUnconfirmedNet = isAsset
                ? unconfirmedDC[0].subtract(unconfirmedDC[1])
                : unconfirmedDC[1].subtract(unconfirmedDC[0]);

        BigDecimal childTotal = BigDecimal.ZERO;
        BigDecimal childConfirmed = BigDecimal.ZERO;

        int placeholderIndex = out.size();
        AccountBalanceDto self = new AccountBalanceDto();
        self.setAccountId(account.getId());
        self.setCode(account.getCode());
        self.setName(account.getName());
        self.setParentId(account.getParent() != null ? account.getParent().getId() : null);
        self.setDepth(depth);
        self.setLeaf(children.isEmpty());
        out.add(self); // 자리 먼저 추가 (부모가 자식보다 먼저 나오도록), 금액은 아래에서 채움

        for (Account child : children) {
            BigDecimal[] childResult = rollup(child, childrenOf, ownConfirmed, ownUnconfirmed, isAsset, depth + 1, out);
            childTotal = childTotal.add(childResult[0]);
            childConfirmed = childConfirmed.add(childResult[1]);
        }

        BigDecimal totalAmount = ownConfirmedNet.add(ownUnconfirmedNet).add(childTotal);
        BigDecimal confirmedAmount = ownConfirmedNet.add(childConfirmed);
        BigDecimal unconfirmedAmount = totalAmount.subtract(confirmedAmount);

        self.setConfirmedAmount(confirmedAmount);
        self.setUnconfirmedAmount(unconfirmedAmount);
        self.setTotalAmount(totalAmount);

        return new BigDecimal[]{totalAmount, confirmedAmount};
    }
}
