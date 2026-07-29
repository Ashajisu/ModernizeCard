package com.test.zoom.dto.journal.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountCreateRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    /** ASSET/LIABILITY/EQUITY/REVENUE/EXPENSE */
    @NotNull
    private String category;

    /** 상위 계정 ID (없으면 최상위) */
    private Long parentId;

    private Integer sortOrder;
}
