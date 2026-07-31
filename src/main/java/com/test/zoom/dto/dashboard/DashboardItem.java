package com.test.zoom.dto.dashboard;

import com.test.zoom.entity.journal.Account;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

/**
 * 대시보드 커스텀 위젯 — 회계 계정 분류와 무관하게 사용자가 원하는 계정만 골라 보여주기 위한 테이블.
 * account_code는 account 테이블의 PK(id)가 아니라 UNIQUE 컬럼인 code를 참조한다.
 */
@Data
@Entity
@Table(name = "dashboard_item")
public class DashboardItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_code", referencedColumnName = "code", nullable = false)
    private Account account;

    @Column(name = "visible")
    @ColumnDefault("1")
    private boolean visible = true;

    @Column(name = "sort_order")
    @ColumnDefault("0")
    private Integer sortOrder = 0;
}
