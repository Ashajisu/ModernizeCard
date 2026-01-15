package com.test.zoom.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.test.zoom.service.MultiDateTimeDeserializer;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "shinhan_card")
@Data
public class SHCardTransaction implements BaseCardTransaction{

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

    @Column(name = "usage_type", nullable = false)
    private String usageType;

    @Column(name = "currency", nullable = false)
    @ColumnDefault("0L")
    private Long currency = 0L;

    @Column(name = "payment_date")
    @JsonDeserialize(using = MultiDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDate;

    @Column(name = "deleted")
    @ColumnDefault("false")
    private boolean deleted = false;
}
