package com.test.zoom.dto.journal.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountUpdateRequest {

    @NotBlank
    private String name;

    @NotNull
    private String category;

    private Long parentId;

    private Integer sortOrder;

    private boolean active = true;
}
