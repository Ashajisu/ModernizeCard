package com.test.zoom.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ExpenseChartItemResponse {

    private String label;
    private BigDecimal cost;
}
