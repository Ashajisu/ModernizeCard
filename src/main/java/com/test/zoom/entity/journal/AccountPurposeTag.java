package com.test.zoom.entity.journal;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table( name = "account_purpose_tag", 
        uniqueConstraints = {@UniqueConstraint( name = "uk_account_tag", columnNames = {"account_id", "fund_source", "account_type"})})
public class AccountPurposeTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
    
    //자금원 표시: 비상금 | 급여 | 더치페이 | 미지정
    @Column(name = "fund_source", nullable = false, length = 50)
    private String fundSource;    
    
    //성격 표시: 적금 | 예금 | 대출 | 미지정
    @Column(name = "account_type", nullable = false, length = 50)
    private String accountType;
    
    @Column(name = "memo", length = 255)
    private String memo;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}