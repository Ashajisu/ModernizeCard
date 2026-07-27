package com.test.zoom.entity.journal;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

/** 카드사별 결제(자동이체) 계좌 매핑 */
@Data
@Entity
@Table(name = "card_settlement_account")
public class CardSettlementAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_company_code", nullable = false, unique = true)
    private String cardCompanyCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "is_active")
    @ColumnDefault("1")
    private boolean active = true;
}
