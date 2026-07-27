package com.test.zoom.entity.journal;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

/** 가맹점명 키워드 → 계정과목 / 가족구성원 태그 매핑 규칙 */
@Data
@Entity
@Table(name = "merchant_category_rule")
public class MerchantCategoryRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "keyword", nullable = false)
    private String keyword;

    @Enumerated(EnumType.STRING)
    @Column(name = "rule_type", nullable = false)
    private RuleType ruleType = RuleType.ACCOUNT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "member_tag_value")
    private String memberTagValue;

    @Column(name = "priority")
    @ColumnDefault("0")
    private Integer priority = 0;

    @Column(name = "is_active")
    @ColumnDefault("1")
    private boolean active = true;

    public enum RuleType {
        ACCOUNT, MEMBER_TAG, POINT_SETTLEMENT
    }
}
