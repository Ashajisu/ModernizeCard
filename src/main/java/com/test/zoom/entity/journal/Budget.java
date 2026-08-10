package com.test.zoom.entity.journal;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/** 월별(yearMonth) 계정과목별 예산 */
@Data
@Entity
@Table(name = "budget")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** "yyyy-MM" */
    @Column(name = "budget_month", nullable = false)
    private String yearMonth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "budget_amount", nullable = false)
    private BigDecimal budgetAmount;
}
