package com.test.zoom.dto.dashboard;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 계정당 1행이므로 상위에 dashboardTag 래퍼가 없다 — 각 행이 자기 태그를 갖는다.
 * 화면에서 여러 탭에 걸친 항목을 한 번에 수정한 뒤 통째로 저장한다.
 */
@Data
public class DashboardSettingUpdateRequest {

    @Valid
    @NotEmpty
    private List<Row> items;

    @Data
    public static class Row {
        @NotBlank
        private String accountCode;
        @NotBlank
        private String dashboardTag;
        private boolean checked;
        private Integer sortOrder;
    }
}