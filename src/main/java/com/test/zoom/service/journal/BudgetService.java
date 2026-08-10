package com.test.zoom.service.journal;

import com.test.zoom.dto.journal.IncomeStatementResponse;
import com.test.zoom.dto.journal.request.BudgetSetRequest;
import com.test.zoom.dto.journal.response.*;
import com.test.zoom.entity.journal.Account;
import com.test.zoom.entity.journal.Budget;
import com.test.zoom.repository.journal.AccountRepository;
import com.test.zoom.repository.journal.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 월별 예산 대비 지출 통계.
 * 실적(actual)은 IncomeStatementService(손익계산서, 기간 손익)를 그대로 재사용한다 —
 * 별도의 집계 로직을 새로 만들지 않고 기존 계산 결과를 조합만 한다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final AccountRepository accountRepository;
    private final IncomeStatementService incomeStatementService;

    @Transactional(readOnly = true)
    public BudgetStatsResponse getStats(YearMonth yearMonth) {
        String ymKey = yearMonth.toString(); // "2026-07"

        Map<Long, BigDecimal> budgetByAccountId = budgetRepository.findByYearMonth(ymKey).stream()
                .collect(Collectors.toMap(b -> b.getAccount().getId(), Budget::getBudgetAmount));

        IncomeStatementResponse income = incomeStatementService.generate(
                yearMonth.atDay(1), yearMonth.atEndOfMonth());
        Map<Long, BigDecimal> actualByAccountId = income.getExpenses().stream()
                .filter(AccountBalanceDto::isLeaf)
                .collect(Collectors.toMap(AccountBalanceDto::getAccountId, AccountBalanceDto::getTotalAmount));

        // 예산이 설정된 계정 + 실제 지출이 있는 계정의 합집합을 표시 대상으로 삼는다
        Set<Long> accountIds = new HashSet<>();
        accountIds.addAll(budgetByAccountId.keySet());
        accountIds.addAll(actualByAccountId.keySet());

        Map<Long, Account> accountMap = accountRepository.findAllById(accountIds).stream()
                .collect(Collectors.toMap(Account::getId, a -> a));

        List<BudgetItemResponse> items = accountIds.stream()
                .map(id -> {
                    Account a = accountMap.get(id);
                    BigDecimal budget = budgetByAccountId.getOrDefault(id, BigDecimal.ZERO);
                    BigDecimal actual = actualByAccountId.getOrDefault(id, BigDecimal.ZERO);
                    BigDecimal remaining = budget.subtract(actual);
                    BigDecimal usageRate = budget.compareTo(BigDecimal.ZERO) > 0
                            ? actual.divide(budget, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                            : null;
                    return new BudgetItemResponse(id, a.getCode(), a.getName(), budget, actual, remaining, usageRate);
                })
                .sorted(Comparator.comparing(BudgetItemResponse::getAccountCode))
                .toList();

        BigDecimal totalBudget = items.stream().map(BudgetItemResponse::getBudgetAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalActual = items.stream().map(BudgetItemResponse::getActualAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new BudgetStatsResponse(ymKey, items, totalBudget, totalActual, totalBudget.subtract(totalActual));
    }

    /** 예산 설정(편집) 다이얼로그용 — 전체 EXPENSE leaf 계정 + 현재 설정된 예산액(없으면 0) */
    @Transactional(readOnly = true)
    public List<BudgetEditItemResponse> getEditList(YearMonth yearMonth) {
        String ymKey = yearMonth.toString();

        List<Account> allAccounts = accountRepository.findAll();
        Set<Long> parentIds = new HashSet<>();
        allAccounts.forEach(a -> {
            if (a.getParent() != null) parentIds.add(a.getParent().getId());
        });

        List<Account> expenseLeafAccounts = allAccounts.stream()
                .filter(a -> a.getCategory() == Account.AccountCategory.EXPENSE)
                .filter(a -> !parentIds.contains(a.getId()))
                .sorted(Comparator.comparing(Account::getCode))
                .toList();

        Map<Long, BigDecimal> budgetByAccountId = budgetRepository.findByYearMonth(ymKey).stream()
                .collect(Collectors.toMap(b -> b.getAccount().getId(), Budget::getBudgetAmount));

        return expenseLeafAccounts.stream()
                .map(a -> new BudgetEditItemResponse(
                        a.getId(), a.getCode(), a.getName(),
                        budgetByAccountId.getOrDefault(a.getId(), BigDecimal.ZERO)))
                .toList();
    }

    /** 예산 upsert */
    public void setBudgets(BudgetSetRequest request) {
        for (BudgetSetRequest.Row row : request.getItems()) {
            Account account = accountRepository.findById(row.getAccountId())
                    .orElseThrow(() -> new IllegalArgumentException("계정을 찾을 수 없습니다: " + row.getAccountId()));

            Budget budget = budgetRepository.findByYearMonthAndAccount_Id(request.getYearMonth(), row.getAccountId())
                    .orElseGet(() -> {
                        Budget created = new Budget();
                        created.setYearMonth(request.getYearMonth());
                        created.setAccount(account);
                        return created;
                    });
            budget.setBudgetAmount(row.getBudgetAmount());
            budgetRepository.save(budget);
        }
    }
}
