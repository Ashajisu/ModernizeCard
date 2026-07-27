package com.test.zoom.service.journal;

import com.test.zoom.entity.journal.JournalEntry;
import com.test.zoom.entity.journal.Loan;
import com.test.zoom.entity.journal.RecurringJournalRule;
import com.test.zoom.repository.journal.JournalEntryRepository;
import com.test.zoom.repository.journal.LoanRepository;
import com.test.zoom.repository.journal.RecurringJournalRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Stream;

/**
 * 정기(반복) 자동 분개 배치.
 * - SAVINGS_TRANSFER / FIXED_EXPENSE: recurring_journal_rule의 고정금액으로 생성
 * - LOAN_REPAYMENT: 연계된 loan의 현재 잔액 기준으로 원금/이자를 매번 재계산
 * 모두 source=RECURRING, is_confirmed=false로 생성 — 실제 이체/납부 확인 전까지 미확인 상태 유지.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RecurringJournalBatchService {

    private final RecurringJournalRuleRepository ruleRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final LoanRepository loanRepository;

    @Transactional
    public int generateForDate(LocalDate today) {
        List<RecurringJournalRule> monthlyRules = ruleRepository
                .findByActiveTrueAndFrequencyAndDayOfMonth(RecurringJournalRule.Frequency.MONTHLY, today.getDayOfMonth());

        List<RecurringJournalRule> yearlyRules = ruleRepository
                .findByActiveTrueAndFrequencyAndMonthOfYearAndDayOfMonth(
                        RecurringJournalRule.Frequency.YEARLY, today.getMonthValue(), today.getDayOfMonth());

        int created = 0;
        for (RecurringJournalRule rule : Stream.concat(monthlyRules.stream(), yearlyRules.stream()).toList()) {
            if (isAlreadyGeneratedThisPeriod(rule, today)) continue;

            if (rule.getRuleType() == RecurringJournalRule.RuleType.LOAN_REPAYMENT) {
                generateLoanRepayment(rule, today);
            } else {
                generateFixedAmountEntry(rule, today);
            }
            created++;
        }
        log.info("정기 분개 생성 완료: {}건 ({})", created, today);
        return created;
    }

    private boolean isAlreadyGeneratedThisPeriod(RecurringJournalRule rule, LocalDate today) {
        LocalDate from, to;
        if (rule.getFrequency() == RecurringJournalRule.Frequency.YEARLY) {
            from = LocalDate.of(today.getYear(), 1, 1);
            to = LocalDate.of(today.getYear(), 12, 31);
        } else {
            YearMonth ym = YearMonth.from(today);
            from = ym.atDay(1);
            to = ym.atEndOfMonth();
        }
        return journalEntryRepository.existsBySourceAndSourceRefIdAndEntryDateBetween(
                JournalEntry.Source.RECURRING, rule.getId(), from, to);
    }

    private void generateFixedAmountEntry(RecurringJournalRule rule, LocalDate today) {
        JournalEntry entry = new JournalEntry();
        entry.setEntryDate(today);
        entry.setDescription(rule.getRuleName());
        entry.setSource(JournalEntry.Source.RECURRING);
        entry.setSourceRefId(rule.getId());
        entry.setConfirmed(false); // 실제 이체 확인 전까지 미확인

        BigDecimal amount = rule.getAmount();
        entry.addLine(rule.getDebitAccount(), amount, null, rule.getMemo());
        entry.addLine(rule.getCreditAccount(), null, amount, rule.getMemo());

        journalEntryRepository.save(entry);
    }

    private void generateLoanRepayment(RecurringJournalRule rule, LocalDate today) {
        Loan loan = rule.getLoan();
        if (loan == null || !loan.isActive()) return;

        BigDecimal interest = loan.calculateThisMonthInterest();
        BigDecimal principal = loan.calculateThisMonthPrincipal(interest);
        BigDecimal totalPayment = principal.add(interest);

        JournalEntry entry = new JournalEntry();
        entry.setEntryDate(today);
        entry.setDescription(rule.getRuleName() + " (원금 " + principal + " / 이자 " + interest + ")");
        entry.setSource(JournalEntry.Source.RECURRING);
        entry.setSourceRefId(rule.getId());
        entry.setConfirmed(false);

        entry.addLine(loan.getLiabilityAccount(), principal, null, "원금상환");
        entry.addLine(loan.getInterestExpenseAccount(), interest, null, "이자비용");
        entry.addLine(loan.getRepaymentAccount(), null, totalPayment, "대출 원리금 자동이체");

        journalEntryRepository.save(entry);

        loan.applyRepayment(principal);
        loanRepository.save(loan);
    }
}
