package com.test.zoom.entity.card;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.test.zoom.service.MultiDateTimeDeserializer;
import jakarta.persistence.*;

/** 전표 조회 등 "카드사 무관 공통 화면"에서 쓰는 통일된 뷰 → 공통 + 상세 필드만 **/
@Data
@MappedSuperclass
public abstract class CardTransaction implements BaseCardTransaction {
    // ── 공통 필드 (필수) ──
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_date", nullable = false)
    @JsonDeserialize(using = MultiDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime transactionDate;

    @Column(name = "payment_date", nullable = false)
    @JsonDeserialize(using = MultiDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDate;

    @Column(name = "usage_type")
    private String usageType;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "currency")
    @ColumnDefault("0")
    private Long currency = 0L;

    @Column(name = "deleted")
    @ColumnDefault("false")
    private boolean deleted = false;

    // ── 상세 필드 (필수) ──
    @Column(name = "used_card")
    private String usedCard;

    @Column(name = "merchant_name")
    private String merchantName;

    @Column(name = "benefit_type")
    private String benefitType;

    @Column(name = "benefit_amount")
    private Long benefitAmount;

    @Column(name = "purchase_type")
    @ColumnDefault("'일시불'")
    private String purchaseType;
    
    // ... 할부 필드
//    private String purchaseType;
//    private Long installmentTotal;
//    private Integer installmentMonths;
//    private Integer installmentNumber;
//    private Long balanceAfterDeposit;
}