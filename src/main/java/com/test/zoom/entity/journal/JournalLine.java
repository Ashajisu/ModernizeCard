package com.test.zoom.entity.journal;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/** 분개 라인 (차변 또는 대변 한쪽에만 금액이 들어감) */
@Data
@Entity
@Table(name = "journal_line")
public class JournalLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_entry_id", nullable = false)
    private JournalEntry journalEntry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "debit_amount")
    private BigDecimal debitAmount = BigDecimal.ZERO;

    @Column(name = "credit_amount")
    private BigDecimal creditAmount = BigDecimal.ZERO;

    @Column(name = "memo")
    private String memo;
}
