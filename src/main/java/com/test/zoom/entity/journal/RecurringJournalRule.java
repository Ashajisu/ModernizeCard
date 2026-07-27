package com.test.zoom.entity.journal;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

/** 정기(반복) 자동 분개 규칙 — 적금이체 / 고정지출 / 대출상환(대출은 loan 테이블과 연계) */
@Data
@Entity
@Table(name = "recurring_journal_rule")
public class RecurringJournalRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rule_name", nullable = false)
    private String ruleName;

    @Enumerated(EnumType.STRING)
    @Column(name = "rule_type", nullable = false)
    private RuleType ruleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequency", nullable = false)
    @ColumnDefault("'MONTHLY'")
    private Frequency frequency = Frequency.MONTHLY;

    /** frequency = YEARLY 일 때만 사용 (1~12) */
    @Column(name = "month_of_year")
    private Integer monthOfYear;

    @Column(name = "day_of_month", nullable = false)
    private Integer dayOfMonth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "debit_account_id", nullable = false)
    private Account debitAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_account_id", nullable = false)
    private Account creditAccount;

    /** 고정금액 — LOAN_REPAYMENT는 loan 테이블에서 매월 재계산하므로 여기는 초기 근사치만 참고 */
    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "principal_amount")
    private BigDecimal principalAmount;

    @Column(name = "interest_amount")
    private BigDecimal interestAmount;

    /** LOAN_REPAYMENT 유형일 때 상환스케줄 계산 대상 대출 (nullable — 대출 없는 규칙도 있음) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id")
    private Loan loan;

    @Column(name = "is_active")
    @ColumnDefault("1")
    private boolean active = true;

    @Column(name = "memo")
    private String memo;

    public enum RuleType {
        SAVINGS_TRANSFER, LOAN_REPAYMENT, FIXED_EXPENSE
    }

    public enum Frequency {
        MONTHLY, YEARLY
    }
}
