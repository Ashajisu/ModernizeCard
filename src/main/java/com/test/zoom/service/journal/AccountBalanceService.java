package com.test.zoom.service.journal;

import com.test.zoom.dto.journal.response.AccountBalanceDto;
import com.test.zoom.dto.journal.response.AccountBalanceResponse;
import com.test.zoom.entity.journal.Account;
import com.test.zoom.repository.journal.AccountRepository;
import com.test.zoom.repository.journal.JournalLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * 계정별 잔액(누적) — 대차대조표는 자산/부채만 다루지만, 여기서는 자본/수익/비용을 포함한
 * 전체 5개 분류의 계정별 누적잔액을 계산한다. (예: 올해 누적 지출이 계정별로 얼마인지 등)
 */
@Service
@RequiredArgsConstructor
public class AccountBalanceService {

    private final AccountRepository accountRepository;
    private final JournalLineRepository journalLineRepository;

    private static final Set<Account.AccountCategory> DEBIT_NORMAL =
            EnumSet.of(Account.AccountCategory.ASSET, Account.AccountCategory.EXPENSE);

    public AccountBalanceResponse generate(LocalDate asOfDate) {
        List<Account> allAccounts = accountRepository.findAll();

        Map<Long, BigDecimal[]> ownConfirmed = new HashMap<>();
        Map<Long, BigDecimal[]> ownUnconfirmed = new HashMap<>();
        for (Object[] row : journalLineRepository.aggregateBalancesAsOf(asOfDate)) {
            Long accountId = (Long) row[0];
            Boolean confirmed = (Boolean) row[1];
            BigDecimal debit = (BigDecimal) row[2];
            BigDecimal credit = (BigDecimal) row[3];
            (Boolean.TRUE.equals(confirmed) ? ownConfirmed : ownUnconfirmed)
                    .put(accountId, new BigDecimal[]{debit, credit});
        }

        Map<Long, List<Account>> childrenOf = new HashMap<>();
        for (Account a : allAccounts) {
            Long parentId = a.getParent() != null ? a.getParent().getId() : null;
            childrenOf.computeIfAbsent(parentId, k -> new ArrayList<>()).add(a);
        }

        AccountBalanceResponse response = new AccountBalanceResponse();
        response.setAsOfDate(asOfDate);
        response.setAssets(buildCategory(Account.AccountCategory.ASSET, childrenOf, ownConfirmed, ownUnconfirmed));
        response.setLiabilities(buildCategory(Account.AccountCategory.LIABILITY, childrenOf, ownConfirmed, ownUnconfirmed));
        response.setEquity(buildCategory(Account.AccountCategory.EQUITY, childrenOf, ownConfirmed, ownUnconfirmed));
        response.setRevenues(buildCategory(Account.AccountCategory.REVENUE, childrenOf, ownConfirmed, ownUnconfirmed));
        response.setExpenses(buildCategory(Account.AccountCategory.EXPENSE, childrenOf, ownConfirmed, ownUnconfirmed));
        return response;
    }

    private List<AccountBalanceDto> buildCategory(
            Account.AccountCategory category,
            Map<Long, List<Account>> childrenOf,
            Map<Long, BigDecimal[]> ownConfirmed,
            Map<Long, BigDecimal[]> ownUnconfirmed) {

        boolean debitNormal = DEBIT_NORMAL.contains(category);
        List<AccountBalanceDto> out = new ArrayList<>();

        List<Account> roots = childrenOf.getOrDefault(null, List.of()).stream()
                .filter(a -> a.getCategory() == category)
                .sorted(Comparator.comparing(Account::getSortOrder))
                .toList();

        for (Account root : roots) {
            rollup(root, childrenOf, ownConfirmed, ownUnconfirmed, debitNormal, 0, out);
        }
        return out;
    }

    private BigDecimal[] rollup(Account account, Map<Long, List<Account>> childrenOf,
                                 Map<Long, BigDecimal[]> ownConfirmed, Map<Long, BigDecimal[]> ownUnconfirmed,
                                 boolean debitNormal, int depth, List<AccountBalanceDto> out) {

        List<Account> children = childrenOf.getOrDefault(account.getId(), List.of()).stream()
                .sorted(Comparator.comparing(Account::getSortOrder)).toList();

        BigDecimal[] confirmedDC = ownConfirmed.getOrDefault(account.getId(), new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});
        BigDecimal[] unconfirmedDC = ownUnconfirmed.getOrDefault(account.getId(), new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});

        BigDecimal ownConfirmedNet = debitNormal
                ? confirmedDC[0].subtract(confirmedDC[1])
                : confirmedDC[1].subtract(confirmedDC[0]);
        BigDecimal ownUnconfirmedNet = debitNormal
                ? unconfirmedDC[0].subtract(unconfirmedDC[1])
                : unconfirmedDC[1].subtract(unconfirmedDC[0]);

        AccountBalanceDto self = new AccountBalanceDto();
        self.setAccountId(account.getId());
        self.setCode(account.getCode());
        self.setName(account.getName());
        self.setParentId(account.getParent() != null ? account.getParent().getId() : null);
        self.setDepth(depth);
        self.setLeaf(children.isEmpty());
        out.add(self);

        BigDecimal childTotal = BigDecimal.ZERO;
        BigDecimal childConfirmed = BigDecimal.ZERO;
        for (Account child : children) {
            BigDecimal[] childResult = rollup(child, childrenOf, ownConfirmed, ownUnconfirmed, debitNormal, depth + 1, out);
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
