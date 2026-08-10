package com.test.zoom.dto.journal.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BudgetSetRequest {

    /** "yyyy-MM" */
    @NotBlank
    private String yearMonth;

    @Valid
    @NotEmpty
    private List<Row> items;

    @Data
    public static class Row {
        @NotNull
        private Long accountId;

        @NotNull
        private BigDecimal budgetAmount;
    }
}
