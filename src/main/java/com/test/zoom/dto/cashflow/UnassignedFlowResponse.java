package com.test.zoom.dto.cashflow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/** fundSource 태그가 없는(미분류) 계정들의 지출 — 신규 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnassignedFlowResponse {
    private List<CashFlowLineDto> items;
    private BigDecimal totalOutflow;
}
