package com.test.zoom.entity.entry;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(catalog = "ledger", name = "account")
/** 계좌/카드/현금 등의 계정 정보 */
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountName;
    private String accountCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType; // ASSET, LIABILITY, INCOME, EXPENSE, EQUITY

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryType categoryType; // BANK, CARD, CASH, ETC, RECEIVABLE

    private String bankName;

    private String accountNoMasked;

    @Column(nullable = false)
    private boolean isActive = true;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Entry> entries;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    // getters & setters
}
