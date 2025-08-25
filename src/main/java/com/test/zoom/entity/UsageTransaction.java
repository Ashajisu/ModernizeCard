package com.test.zoom.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.test.zoom.service.MultiDateTimeDeserializer;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "unified_card_view")
public class UsageTransaction{

    @EmbeddedId
    private UsageTransactionKey key;

//    @Column(name = "id")
//    private Long id;

    // 지출종류
//    @Column(name = "card_company")
//    private String cardCompany;

    // 이용일 -> 거래일
    @Column(name = "transaction_date")
    @JsonDeserialize(using = MultiDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime transactionDate;

    // 이용구분
    @Column(name = "usage_type")
    private String usageType;

    // 가맹점
    @Column(name = "merchant_name")
    private String merchantName;

    // 일시불/할부
    @Column(name = "purchase_type")
    private String purchaseType;

    // 원금
    @Column(name = "currency")
    private Long currency;
}
