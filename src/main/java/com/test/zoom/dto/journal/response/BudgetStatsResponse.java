package com.test.zoom.dto.journal.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetStatsResponse {
    private String yearMonth;
    private List<BudgetItemResponse> items;
    private BigDecimal totalBudget;
    private BigDecimal totalActual;
    private BigDecimal totalRemaining;
}
