package com.test.zoom.entity.journal;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * 대출 원리금균등상환 관리.
 * 매월 배치 시점의 잔액(currentBalance) 기준으로 이자/원금을 재계산한다.
 */
@Data
@Entity
@Table(name = "loan")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    /** 부채 계정 (예: 대출금_우리은행) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liability_account_id", nullable = false)
    private Account liabilityAccount;

    /** 상환 출금 계좌 (예: 우리급여) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repayment_account_id", nullable = false)
    private Account repaymentAccount;

    /** 이자비용 계정 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interest_expense_account_id", nullable = false)
    private Account interestExpenseAccount;

    @Column(name = "principal_amount", nullable = false)
    private BigDecimal principalAmount;

    /** 연이율 (%), 예: 2.75 */
    @Column(name = "annual_rate", nullable = false)
    private BigDecimal annualRate;

    @Column(name = "term_months", nullable = false)
    private Integer termMonths;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "repayment_day", nullable = false)
    private Integer repaymentDay;

    /** 원리금균등 고정 월상환액 (매월 동일, 원금+이자 합계) */
    @Column(name = "monthly_payment", nullable = false)
    private BigDecimal monthlyPayment;

    /** 현재 남은 대출 잔액 — 매월 상환 처리 후 갱신됨 */
    @Column(name = "current_balance", nullable = false)
    private BigDecimal currentBalance;

    @Column(name = "is_active")
    private boolean active = true;

    /**
     * 원리금균등상환 월 납입액 계산.
     * payment = P * r(1+r)^n / ((1+r)^n - 1),  r = 연이율/12
     */
    public static BigDecimal calculateMonthlyPayment(BigDecimal principal, BigDecimal annualRatePercent, int termMonths) {
        double p = principal.doubleValue();
        double r = annualRatePercent.doubleValue() / 100.0 / 12.0;
        double n = termMonths;
        double payment = p * r * Math.pow(1 + r, n) / (Math.pow(1 + r, n) - 1);
        return BigDecimal.valueOf(payment).setScale(0, RoundingMode.HALF_UP);
    }

    /** 이번 회차 이자분 = 현재잔액 × (연이율/12) */
    public BigDecimal calculateThisMonthInterest() {
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);
        return currentBalance.multiply(monthlyRate).setScale(0, RoundingMode.HALF_UP);
    }

    /** 이번 회차 원금분 = 월상환액 − 이자분 (마지막 회차는 잔액 전액) */
    public BigDecimal calculateThisMonthPrincipal(BigDecimal interestThisMonth) {
        BigDecimal principalThisMonth = monthlyPayment.subtract(interestThisMonth);
        if (principalThisMonth.compareTo(currentBalance) > 0) {
            principalThisMonth = currentBalance; // 마지막 회차 등 잔액보다 큰 경우 보정
        }
        return principalThisMonth;
    }

    /** 상환 처리 후 잔액 갱신 */
    public void applyRepayment(BigDecimal principalThisMonth) {
        this.currentBalance = this.currentBalance.subtract(principalThisMonth);
        if (this.currentBalance.compareTo(BigDecimal.ZERO) <= 0) {
            this.currentBalance = BigDecimal.ZERO;
            this.active = false;
        }
    }
}
