package com.test.zoom.entity.journal;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

/** 계정과목 (Chart of Accounts) */
@Data
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private AccountCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Account parent;

    @Column(name = "is_active")
    @ColumnDefault("1")
    private boolean active = true;

    @Column(name = "sort_order")
    @ColumnDefault("0")
    private Integer sortOrder = 0;

    public enum AccountCategory {
        ASSET, LIABILITY, EQUITY, REVENUE, EXPENSE
    }
}