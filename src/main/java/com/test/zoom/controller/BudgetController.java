package com.test.zoom.controller;

import com.test.zoom.dto.journal.request.BudgetSetRequest;
import com.test.zoom.dto.journal.response.BudgetEditItemResponse;
import com.test.zoom.dto.journal.response.BudgetStatsResponse;
import com.test.zoom.service.journal.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/journal/budget")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    /** GET /journal/budget?yearMonth=2026-07 (생략 시 이번달) — 예산 대비 실적 통계 */
    @GetMapping
    public BudgetStatsResponse getStats(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth) {
        return budgetService.getStats(yearMonth != null ? yearMonth : YearMonth.now());
    }

    /** GET /journal/budget/edit?yearMonth=2026-07 — 예산 설정 다이얼로그용 전체 계정 목록 */
    @GetMapping("/edit")
    public List<BudgetEditItemResponse> getEditList(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth) {
        return budgetService.getEditList(yearMonth);
    }

    /** PUT /journal/budget — 예산 저장 */
    @PutMapping
    public ResponseEntity<Void> setBudgets(@Valid @RequestBody BudgetSetRequest request) {
        budgetService.setBudgets(request);
        return ResponseEntity.ok().build();
    }
}
