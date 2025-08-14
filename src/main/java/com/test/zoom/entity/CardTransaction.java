package com.test.zoom.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.test.zoom.service.MultiDateTimeDeserializer;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "card_transactions")
@Data
public class CardTransaction {

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

    @Column(name = "amount", precision = 15, scale = 2)
    private Integer amount;

    @Column(name = "purchase_type")
    private String purchaseType;

    @Column(name = "usage_type")
    private String usageType;

    @Column(name = "currency")
    private String currency;

    @Column(name = "payment_date")
    @JsonDeserialize(using = MultiDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDate;

    @Column(name = "deleted")
    private boolean deleted;
}
