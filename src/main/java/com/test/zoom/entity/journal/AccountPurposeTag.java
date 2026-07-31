package com.test.zoom.entity.journal;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table( name = "account_purpose_tag", 
        uniqueConstraints = {@UniqueConstraint( name = "uk_account_tag", columnNames = {"account_id", "tag_name"})})
public class AccountPurposeTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
    
    @Column(name = "tag_name", nullable = false, length = 50)
    private String tagName;
    
    @Column(name = "memo", length = 255)
    private String memo;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}