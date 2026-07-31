package com.test.zoom.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.ErrorResponse;

import java.math.BigDecimal;

/**
 * PaymentGateways.vue 디자인에 맞춘 표시용 DTO.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardItemResponse {
    private String title;      // 계정명
    private String subtitle;   // 태그명
    private BigDecimal amount; // 부호 포함 포맷팅된 금액 문자열 (예: "+1,250,000", "-320,000")
    private boolean negative;  //true = 지출/부채 등 음수 표시
    private String accountCode;
    private String category; //계정 카테고리 예) ASSET, LIABILITY
    private Integer sortOrder; //정렬 순서
}