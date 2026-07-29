package com.test.zoom.service.journal;

import com.test.zoom.dto.journal.response.AccountBalanceDto;
import com.test.zoom.dto.journal.IncomeStatementResponse;
import com.test.zoom.entity.journal.Account;
import com.test.zoom.repository.journal.AccountRepository;
import com.test.zoom.repository.journal.JournalLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * 손익계산서 산출 (기간 손익, 누적 아님).
 * - EXPENSE: 기간 내 차변합 - 대변합 (환급 등으로 대변이 생길 수 있음을 대비)
 * - REVENUE: 기간 내 대변합 - 차변합
 * ⚠️ 정기분개(가족회비 등)는 is_confirmed=false로 생성되는데, 이는 "계좌출금 미확인"일 뿐
 *    비용 자체는 실제 발생한 것이므로 확정여부와 무관하게 전액 반영한다.
 */
@Service
@RequiredArgsConstructor
public class IncomeStatementService {

    private final AccountRepository accountRepository;
    private final JournalLineRepository journalLineRepository;

    public IncomeStatementResponse generate(LocalDate from, LocalDate to) {
        List<Account> allAccounts = accountRepository.findAll();
        Map<Long, BigDecimal[]> ownDC = new HashMap<>(); // accountId -> [debit, credit]
        for (Object[] row : journalLineRepository.aggregateBalancesBetween(from, to)) {
            Long accountId = (Long) row[0];
            BigDecimal debit = (BigDecimal) row[1];
            BigDecimal credit = (BigDecimal) row[2];
            ownDC.put(accountId, new BigDecimal[]{debit, credit});
        }

        Map<Long, List<Account>> childrenOf = new HashMap<>();
        for (Account a : allAccounts) {
            Long parentId = a.getParent() != null ? a.getParent().getId() : null;
            childrenOf.computeIfAbsent(parentId, k -> new ArrayList<>()).add(a);
        }

        List<AccountBalanceDto> expenses = new ArrayList<>();
        List<AccountBalanceDto> revenues = new ArrayList<>();

        List<Account> expenseRoots = childrenOf.getOrDefault(null, List.of()).stream()
                .filter(a -> a.getCategory() == Account.AccountCategory.EXPENSE)
                .sorted(Comparator.comparing(Account::getSortOrder)).toList();
        List<Account> revenueRoots = childrenOf.getOrDefault(null, List.of()).stream()
                .filter(a -> a.getCategory() == Account.AccountCategory.REVENUE)
                .sorted(Comparator.comparing(Account::getSortOrder)).toList();

        BigDecimal totalExpense = BigDecimal.ZERO;
        for (Account root : expenseRoots) {
            totalExpense = totalExpense.add(rollup(root, childrenOf, ownDC, true, 0, expenses));
        }
        BigDecimal totalRevenue = BigDecimal.ZERO;
        for (Account root : revenueRoots) {
            totalRevenue = totalRevenue.add(rollup(root, childrenOf, ownDC, false, 0, revenues));
        }

        IncomeStatementResponse response = new IncomeStatementResponse();
        response.setFromDate(from);
        response.setToDate(to);
        response.setExpenses(expenses);
        response.setRevenues(revenues);
        response.setTotalExpense(totalExpense);
        response.setTotalRevenue(totalRevenue);
        response.setNetIncome(totalRevenue.subtract(totalExpense));
        return response;
    }

    private BigDecimal rollup(Account account, Map<Long, List<Account>> childrenOf,
                               Map<Long, BigDecimal[]> ownDC, boolean isExpense, int depth,
                               List<AccountBalanceDto> out) {
        List<Account> children = childrenOf.getOrDefault(account.getId(), List.of()).stream()
                .sorted(Comparator.comparing(Account::getSortOrder)).toList();

        BigDecimal[] dc = ownDC.getOrDefault(account.getId(), new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});
        BigDecimal ownNet = isExpense ? dc[0].subtract(dc[1]) : dc[1].subtract(dc[0]);

        AccountBalanceDto self = new AccountBalanceDto();
        self.setAccountId(account.getId());
        self.setCode(account.getCode());
        self.setName(account.getName());
        self.setParentId(account.getParent() != null ? account.getParent().getId() : null);
        self.setDepth(depth);
        self.setLeaf(children.isEmpty());
        out.add(self);

        BigDecimal childTotal = BigDecimal.ZERO;
        for (Account child : children) {
            childTotal = childTotal.add(rollup(child, childrenOf, ownDC, isExpense, depth + 1, out));
        }

        BigDecimal total = ownNet.add(childTotal);
        self.setTotalAmount(total);
        self.setConfirmedAmount(total); // 손익계산서는 확정여부 구분 없이 전액 반영
        self.setUnconfirmedAmount(BigDecimal.ZERO);

        return total;
    }
}
