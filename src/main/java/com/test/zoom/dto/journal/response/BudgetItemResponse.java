package com.test.zoom.dto.journal.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetItemResponse {
    private Long accountId;
    private String accountCode;
    private String accountName;
    private BigDecimal budgetAmount;
    private BigDecimal actualAmount;
    private BigDecimal remainingAmount; // budget - actual (음수면 초과)

    /** 예산 대비 사용률(%) — 예산이 0(미설정)이면 null */
    private BigDecimal usageRate;
}
