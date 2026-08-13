package com.test.zoom.service.cashflow;

import com.test.zoom.dto.cashflow.*;
import com.test.zoom.entity.journal.Account;
import com.test.zoom.repository.journal.AccountPurposeTagRepository;
import com.test.zoom.repository.journal.AccountRepository;
import com.test.zoom.repository.journal.JournalLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

/**
 * 자금원별(급여/비상금/더치페이/미지정) 월간 현금흐름.
 * AccountPurposeTag(fundSource, accountType) 기반으로 집계 — 전표 생성방식(MANUAL/RECURRING 등)은
 * 조건에 포함하지 않으므로 수기입력/자동생성 구분 없이 동일하게 반영된다.
 * RecurringJournalRule.RuleType 기반 판별 로직은 더 이상 사용하지 않는다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CashFlowService {

    private static final String DUTCH_PAY_CODE = "5300";

    private static final String FUND_SALARY = "급여";
    private static final String FUND_EMERGENCY = "비상금";

    private static final String TYPE_LIVING = "생활비";
    private static final String TYPE_SAVINGS = "저축";
    private static final String TYPE_LOAN = "대출";
    private static final String TYPE_INTEREST = "이자";
    private static final String TYPE_INTEREST_INCOME = "이자수익";
    private static final String TYPE_INCOME = "수입";

    private final AccountRepository accountRepository;
    private final AccountPurposeTagRepository accountPurposeTagRepository;
    private final JournalLineRepository journalLineRepository;

    public CashFlowResponse getCashFlow(YearMonth yearMonth) {
        LocalDate from = yearMonth.atDay(1);
        LocalDate to = yearMonth.atEndOfMonth();

        SalaryFlowResponse salaryFlow = buildSalaryFlow(from, to);
        EmergencyFlowResponse emergencyFlow = buildEmergencyFlow(from, to);
        DutchPayFlowResponse dutchPayFlow = buildDutchPayFlow(from, to);
        UnassignedFlowResponse unassignedFlow = buildUnassignedFlow(from, to);

        BigDecimal dutchPayNet = dutchPayFlow.getSettledAmount().subtract(dutchPayFlow.getPrepaidAmount());
        BigDecimal total = salaryFlow.getRemainingCash()
                .add(emergencyFlow.getNetBalanceChange())
                .add(dutchPayNet)
                .subtract(unassignedFlow.getTotalOutflow());

        return new CashFlowResponse(yearMonth.toString(), salaryFlow, emergencyFlow, dutchPayFlow, unassignedFlow, total);
    }

    // ── 급여 흐름 ──────────────────────────────────────────
    private SalaryFlowResponse buildSalaryFlow(LocalDate from, LocalDate to) {
        BigDecimal salaryIncome = sumCashFlowCreditByPurpose(from, to, FUND_SALARY, TYPE_INCOME);

        List<CashFlowLineDto> livingItems = itemizeByPurpose(from, to, FUND_SALARY, TYPE_LIVING);
        BigDecimal livingTotal = sumCashFlowByPurpose(from, to, FUND_SALARY, TYPE_LIVING);
        BigDecimal savings = sumCashFlowByPurpose(from, to, FUND_SALARY, TYPE_SAVINGS);
        BigDecimal loanPrincipal = sumCashFlowByPurpose(from, to, FUND_SALARY, TYPE_LOAN);
        BigDecimal loanInterest = sumCashFlowByPurpose(from, to, FUND_SALARY, TYPE_INTEREST);

        BigDecimal remaining = salaryIncome
                .subtract(livingTotal).subtract(savings).subtract(loanPrincipal).subtract(loanInterest);

        return new SalaryFlowResponse(
                salaryIncome, livingItems, livingTotal.negate(),
                savings.negate(), loanPrincipal.negate(), loanInterest.negate(), remaining);
    }

    // ── 비상금 흐름 ────────────────────────────────────────
    private EmergencyFlowResponse buildEmergencyFlow(LocalDate from, LocalDate to) {
        List<CashFlowLineDto> livingItems = itemizeByPurpose(from, to, FUND_EMERGENCY, TYPE_LIVING);
        BigDecimal livingTotal = sumCashFlowByPurpose(from, to, FUND_EMERGENCY, TYPE_LIVING);

        BigDecimal interestIncome = sumCashFlowCreditByPurpose(from, to, FUND_EMERGENCY, TYPE_INTEREST_INCOME);
        BigDecimal savings = sumCashFlowByPurpose(from, to, FUND_EMERGENCY, TYPE_SAVINGS);
        BigDecimal loanRepayment = sumCashFlowByPurpose(from, to, FUND_EMERGENCY, TYPE_LOAN);
        BigDecimal interest = sumCashFlowByPurpose(from, to, FUND_EMERGENCY, TYPE_INTEREST);

        BigDecimal netChange = interestIncome
                .subtract(livingTotal).subtract(savings).subtract(loanRepayment).subtract(interest);

        return new EmergencyFlowResponse(
                livingItems, livingTotal.negate(),
                interestIncome, savings.negate(), loanRepayment.negate(), interest.negate(),
                netChange);
    }

    // ── 더치페이 흐름 (더치페이 전용 계정 직접 집계 — 기존 방식 유지) ──
    private DutchPayFlowResponse buildDutchPayFlow(LocalDate from, LocalDate to) {
        Account dutchPayAccount = accountRepository.findByCode(DUTCH_PAY_CODE).orElse(null);
        if (dutchPayAccount == null) {
            return new DutchPayFlowResponse(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        }
        List<Long> ids = List.of(dutchPayAccount.getId());
        BigDecimal prepaid = journalLineRepository.sumDebitAmountByAccountIds(ids, from, to);
        BigDecimal settled = journalLineRepository.sumCreditAmountByAccountIds(ids, from, to);
        return new DutchPayFlowResponse(prepaid, settled, prepaid.subtract(settled));
    }

    // ── 미지정 흐름 — 태그가 아예 없는 EXPENSE 계정들의 지출 ──
    private UnassignedFlowResponse buildUnassignedFlow(LocalDate from, LocalDate to) {
        Set<Long> taggedIds = new HashSet<>(accountPurposeTagRepository.findAllTaggedAccountIds());

        List<CashFlowLineDto> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (Account account : accountRepository.findAll()) {
            if (account.getCategory() != Account.AccountCategory.EXPENSE) continue;
            if (taggedIds.contains(account.getId())) continue;

            BigDecimal debit = journalLineRepository.sumDebitAmountByAccountId(account.getId(), from, to);
            if (debit.compareTo(BigDecimal.ZERO) == 0) continue;

            items.add(new CashFlowLineDto(account.getCode(), account.getName(), debit.negate()));
            total = total.add(debit);
        }

        return new UnassignedFlowResponse(items, total);
    }

    // ── 공통 집계 헬퍼 (요청하신 함수 형태 그대로) ─────────

    /** 전표종류가 아닌 태그 기반으로 합산 (차변) */
    private BigDecimal sumCashFlowByPurpose(LocalDate from, LocalDate to, String fundSource, String accountType) {
        List<Long> accountIds = accountPurposeTagRepository.findAccountIdsByFundSourceAndAccountType(fundSource, accountType);
        if (accountIds.isEmpty()) return BigDecimal.ZERO;
        return journalLineRepository.sumDebitAmountByAccountIds(accountIds, from, to);
    }

    /** 태그 기반 합산 (대변) — 수입/이자수익처럼 수익성 계정일 때 사용 */
    private BigDecimal sumCashFlowCreditByPurpose(LocalDate from, LocalDate to, String fundSource, String accountType) {
        List<Long> accountIds = accountPurposeTagRepository.findAccountIdsByFundSourceAndAccountType(fundSource, accountType);
        if (accountIds.isEmpty()) return BigDecimal.ZERO;
        return journalLineRepository.sumCreditAmountByAccountIds(accountIds, from, to);
    }

    /** 태그 계정별 상세 내역 (차변 기준, 화면 표시용) */
    private List<CashFlowLineDto> itemizeByPurpose(LocalDate from, LocalDate to, String fundSource, String accountType) {
        List<Long> accountIds = accountPurposeTagRepository.findAccountIdsByFundSourceAndAccountType(fundSource, accountType);
        if (accountIds.isEmpty()) return List.of();

        Map<Long, Account> accountMap = new HashMap<>();
        accountRepository.findAllById(accountIds).forEach(a -> accountMap.put(a.getId(), a));

        List<CashFlowLineDto> items = new ArrayList<>();
        for (Long accountId : accountIds) {
            Account account = accountMap.get(accountId);
            if (account == null) continue;
            BigDecimal debit = journalLineRepository.sumDebitAmountByAccountId(accountId, from, to);
            if (debit.compareTo(BigDecimal.ZERO) == 0) continue;
            items.add(new CashFlowLineDto(account.getCode(), account.getName(), debit.negate()));
        }
        return items;
    }
}
