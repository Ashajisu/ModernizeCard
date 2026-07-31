package com.test.zoom.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 설정화면용 — 전체 후보 계정 1건당 1행.
 * dashboardTag는 현재 소속(없으면 "미지정" 기본값), 화면에서 select로 변경 가능.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSettingItemResponse {
    private String accountCode;
    private String accountName;
    private String category;     // ASSET/LIABILITY
    private String dashboardTag; // 급여/비상금/미지정 등 — 계정당 하나만 가짐
    private boolean checked;
    private Integer sortOrder;
}