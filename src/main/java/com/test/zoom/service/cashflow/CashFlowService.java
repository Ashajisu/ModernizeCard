package com.test.zoom.service.cashflow;

import com.test.zoom.dto.cashflow.*;
import com.test.zoom.entity.journal.*;
import com.test.zoom.repository.AccountPurposeTagRepository;
import com.test.zoom.repository.journal.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

/**
 * 자금원별(급여/비상금/더치페이) 월간 현금흐름.
 * 새 컬럼(payer/fundSource) 없이 기존 Account 계층(parentId) + recurring_journal_rule.ruleType +
 * journal_line.account.category 조합만으로 자금원을 구분한다.
 *
 * 기존 로직 재사용:
 * - 계정별 기간 집계: JournalLineRepository.aggregateBalancesBetween() (IncomeStatementService와 동일 쿼리 재사용)
 * - 적금이체/대출상환 식별: RecurringJournalBatchService가 이미 만들어둔 recurring_journal_rule 연결(sourceRefId) 재사용
 * - 더치페이 선지출/정산: CardStatementJournalService(usageType="더치페이" 직접매핑) / BankImportService(usageType="더치페이" 매칭)가
 *   이미 만들어내는 '더치페이' 계정의 분개라인을 그대로 집계 — 별도 정산 로직 신규 작성 없음
 *
 * ⚠️ 카드 지출은 CARD_IMPORT(카드 승인/명세서 확정 시점) 기준으로 집계된다. 실제 계좌 출금(SETTLEMENT)
 *    시점에는 카드미지급금↔예금만 상계되고 '고정생활비'/'더치페이' 등 비용 계정을 다시 건드리지 않으므로
 *    이중계산되지 않는다. 즉 이 화면의 "현금흐름"은 은행 출금 시점이 아니라 카드 확정 시점 기준이다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CashFlowService {

    private static final String SALARY_REVENUE_CODE = "4000";     // 급여(REVENUE)
    private static final String FIXED_LIVING_PARENT_CODE = "5999"; // 급여생활비
    private static final String VARIABLE_LIVING_PARENT_CODE = "5998"; // 비상금생활비
    private static final String DUTCH_PAY_CODE = "5997";          // 더치페이

    public static final String SALARY = "급여";
    public static final String VARIABLE = "비상금";
    public static final String DUTCH = "더치";
    
    public static final String SAVING = "저축";
    public static final String LOAN = "대출";
    public static final String INTEREST= "이자";

    private final AccountRepository accountRepository;
    private final JournalLineRepository journalLineRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final RecurringJournalRuleRepository recurringJournalRuleRepository;
    private final AccountPurposeTagRepository accountPurposeTagRepository;

    public CashFlowResponse getCashFlow(YearMonth yearMonth) {
        LocalDate from = yearMonth.atDay(1);
        LocalDate to = yearMonth.atEndOfMonth();

        // 계정별 기간 차변/대변 합계를 한 번에 조회해서 모든 흐름 계산에 재사용
        Map<Long, BigDecimal[]> debitCreditByAccountId = new HashMap<>();
        for (Object[] row : journalLineRepository.aggregateBalancesBetween(from, to)) {
            debitCreditByAccountId.put((Long) row[0], new BigDecimal[]{(BigDecimal) row[1], (BigDecimal) row[2]});
        }

        SalaryFlowResponse salaryFlow = buildSalaryFlow(from, to, debitCreditByAccountId);
        EmergencyFlowResponse emergencyFlow = buildEmergencyFlow(from, to, debitCreditByAccountId);
        DutchPayFlowResponse dutchPayFlow = buildDutchPayFlow(debitCreditByAccountId);

        BigDecimal dutchPayNet = dutchPayFlow.getSettledAmount().subtract(dutchPayFlow.getPrepaidAmount());
        BigDecimal total = salaryFlow.getRemainingCash()
                .add(emergencyFlow.getUsageTotal())
                .add(dutchPayNet);

        return new CashFlowResponse(yearMonth.toString(), salaryFlow, emergencyFlow, dutchPayFlow, total);
    }

    // ── 급여 흐름 ──────────────────────────────────────────
    private SalaryFlowResponse buildSalaryFlow(LocalDate from, LocalDate to, Map<Long, BigDecimal[]> debitCredit) {
        Account salaryAccount = accountRepository.findByCode(SALARY_REVENUE_CODE).orElse(null);
        BigDecimal salaryIncome = salaryAccount != null
                ? creditNet(debitCredit, salaryAccount.getId()) : BigDecimal.ZERO;

        Account fixedLivingParent = accountRepository.findByCode(FIXED_LIVING_PARENT_CODE).orElse(null);
        List<CashFlowLineDto> fixedLivingItems = new ArrayList<>();
        BigDecimal fixedLivingTotal = BigDecimal.ZERO;
        if (fixedLivingParent != null) {
            for (Account child : accountRepository.findByParent_Id(fixedLivingParent.getId())) {
                BigDecimal debit = debitNet(debitCredit, child.getId());
                if (debit.compareTo(BigDecimal.ZERO) == 0) continue;
                fixedLivingItems.add(new CashFlowLineDto(child.getCode(), child.getName(), debit.negate()));
                fixedLivingTotal = fixedLivingTotal.add(debit);
            }
        }
        
        BigDecimal savingsOutflow = sumCashFlowByPurpose(from, to, SALARY, SAVING);
        BigDecimal loanPrincipalOutflow = sumCashFlowByPurpose(from, to, SALARY, LOAN);
        BigDecimal loanInterestOutflow = sumCashFlowByPurpose(from, to, SALARY, INTEREST);

        BigDecimal remainingCash = salaryIncome
                .subtract(fixedLivingTotal)
                .subtract(savingsOutflow)
                .subtract(loanPrincipalOutflow)
                .subtract(loanInterestOutflow);

        return new SalaryFlowResponse(
                salaryIncome, fixedLivingItems, fixedLivingTotal.negate(),
                savingsOutflow.negate(), loanPrincipalOutflow.negate(), loanInterestOutflow.negate(),
                remainingCash);
    }

    // ── 비상금 흐름 ────────────────────────────────────────
    private EmergencyFlowResponse buildEmergencyFlow(LocalDate from, LocalDate to, Map<Long, BigDecimal[]> debitCredit) {
        Account variableLivingParent = accountRepository.findByCode(VARIABLE_LIVING_PARENT_CODE).orElse(null);
        List<CashFlowLineDto> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        if (variableLivingParent != null) {
            for (Account child : accountRepository.findByParent_Id(variableLivingParent.getId())) {
                BigDecimal debit = debitNet(debitCredit, child.getId());
                if (debit.compareTo(BigDecimal.ZERO) == 0) continue;
                items.add(new CashFlowLineDto(child.getCode(), child.getName(), debit.negate()));
                total = total.add(debit);
            }
        }

        //비상금의 저축, 학자금상환을 확인하기 위함.
//        BigDecimal savingsOutflow = sumCashFlowByPurpose(from, to, VARIABLE, SAVING);
//        BigDecimal loanPrincipalOutflow = sumCashFlowByPurpose(from, to, VARIABLE, LOAN);
//        BigDecimal loanInterestOutflow = sumCashFlowByPurpose(from, to, VARIABLE, INTEREST);
//
//        BigDecimal remainingCash = savingsOutflow
//                .subtract(loanPrincipalOutflow)
//                .subtract(loanInterestOutflow);
        
        return new EmergencyFlowResponse(items, total.negate());
//                savingsOutflow, loanPrincipalOutflow ,loanInterestOutflow, remainingCash);
    }

    // ── 더치페이 흐름 ──────────────────────────────────────
    private DutchPayFlowResponse buildDutchPayFlow(Map<Long, BigDecimal[]> debitCredit) {
        Account dutchPayAccount = accountRepository.findByCode(DUTCH_PAY_CODE).orElse(null);
        if (dutchPayAccount == null) {
            return new DutchPayFlowResponse(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        }
        BigDecimal[] dc = debitCredit.getOrDefault(dutchPayAccount.getId(), new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});
        BigDecimal prepaid = dc[0];   // 차변 = 선지출 (카드 usageType="더치페이" 매칭)
        BigDecimal settled = dc[1];  // 대변 = 정산입금 (은행입금 usageType="더치페이" 매칭)
        return new DutchPayFlowResponse(prepaid, settled, prepaid.subtract(settled));
    }

    // ── 공통 헬퍼 ──────────────────────────────────────────

    private BigDecimal debitNet(Map<Long, BigDecimal[]> debitCredit, Long accountId) {
        BigDecimal[] dc = debitCredit.get(accountId);
        return dc != null ? dc[0].subtract(dc[1]) : BigDecimal.ZERO;
    }

    private BigDecimal creditNet(Map<Long, BigDecimal[]> debitCredit, Long accountId) {
        BigDecimal[] dc = debitCredit.get(accountId);
        return dc != null ? dc[1].subtract(dc[0]) : BigDecimal.ZERO;
    }

    /**
     * 특정 ruleType(적금이체/대출상환)으로 생성된 RECURRING 전표들 중, lineCategory에 해당하는
     * 계정의 차변 금액만 합산한다.
     * (대출상환은 한 전표 안에 LIABILITY 라인(원금)과 EXPENSE 라인(이자)이 함께 있어 category로 분리)
     */
    private BigDecimal sumRecurringByRuleTypeAndLineCategory(
            LocalDate from, LocalDate to,
            RecurringJournalRule.RuleType ruleType, Account.AccountCategory lineCategory) {

        List<Long> ruleIds = recurringJournalRuleRepository.findByRuleType(ruleType).stream()
                .map(RecurringJournalRule::getId)
                .toList();
        if (ruleIds.isEmpty()) return BigDecimal.ZERO;

        List<JournalEntry> entries = journalEntryRepository
                .findBySourceAndSourceRefIdInAndEntryDateBetweenAndDeletedFalse(
                        JournalEntry.Source.RECURRING, ruleIds, from, to);

        BigDecimal sum = BigDecimal.ZERO;
        for (JournalEntry entry : entries) {
            for (JournalLine line : entry.getLines()) {
                if (line.getAccount().getCategory() == lineCategory && line.getDebitAmount() != null) {
                    sum = sum.add(line.getDebitAmount());
                }
            }
        }
        return sum;
    }

    /**
     * 전표종류가 아닌 태그 기반으로 합산
     * **/
    private BigDecimal sumCashFlowByPurpose(
            LocalDate from, LocalDate to, String fundSource, String accountType) {

        List<Long> accountIds =
                accountPurposeTagRepository.findAccountIdsByFundSourceAndAccountType(fundSource, accountType);

        if (accountIds.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return journalLineRepository.sumDebitAmountByAccountIds(
                accountIds,
                from,
                to
        );
    }
}
