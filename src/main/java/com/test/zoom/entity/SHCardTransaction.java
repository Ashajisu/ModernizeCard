package com.test.zoom.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.test.zoom.service.MultiDateTimeDeserializer;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "SHcard")
@Data
public class SHCardTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_date", nullable = false)
    @JsonDeserialize(using = MultiDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime transactionDate;

    @Column(name = "card_type")
    private String cardType;

    @Column(name = "used_card")
    private String usedCard;

    @Column(name = "merchant_name")
    private String merchantName;

    @Column(name = "approval_number")
    private String approvalNumber;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "purchase_type")
    private String purchaseType;

    @Column(name = "usage_type")
    private String usageType;

    @Column(name = "currency")
    private Long currency;

    @Column(name = "payment_date")
    @JsonDeserialize(using = MultiDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDate;

    @Column(name = "deleted")
    private boolean deleted;
}
