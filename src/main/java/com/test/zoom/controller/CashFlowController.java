package com.test.zoom.controller;

import com.test.zoom.dto.cashflow.CashFlowResponse;
import com.test.zoom.service.cashflow.CashFlowService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;

@RestController
@RequiredArgsConstructor
public class CashFlowController {

    private final CashFlowService cashFlowService;

    /** GET /cash-flow?yearMonth=2026-08 (생략 시 이번달) */
    @GetMapping("/cash-flow")
    public CashFlowResponse getCashFlow(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth) {
        return cashFlowService.getCashFlow(yearMonth != null ? yearMonth : YearMonth.now());
    }
}
