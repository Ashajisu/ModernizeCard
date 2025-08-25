package com.test.zoom.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.test.zoom.service.MultiDateTimeDeserializer;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "kookmin_card")
public class KBCardTransaction implements BaseCardTransaction{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이용일 -> 거래일
    @Column(name = "transaction_date")
    @JsonDeserialize(using = MultiDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime transactionDate;

    // 이용구분 -> 카드구분
    @Column(name = "used_card")
    private String usedCard;

    // 이용구분
    @Column(name = "usage_type")
    private String usageType;

    // 가맹점
    @Column(name = "merchant_name")
    private String merchantName;

    // 이용금액
    @Column(name = "amount")
    private Long amount;

    // 이용혜택
    @Column(name = "benefit_type")
    private String benefitType;

    // 일시불/할부
    @Column(name = "purchase_type")
    private String purchaseType;

    // 혜택금액
    @Column(name = "benefit_amount")
    private Long benefitAmount;

    // 원금
    @Column(name = "currency")
    private Long currency;

    // 입금후잔액
    @Column(name = "balance_after_deposit")
    private Long balanceAfterDeposit;

    //결제일
    @Column(name = "payment_date")
    @JsonDeserialize(using = MultiDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDate;

    //삭제여부
    @Column(name = "deleted")
    private boolean deleted;

}
