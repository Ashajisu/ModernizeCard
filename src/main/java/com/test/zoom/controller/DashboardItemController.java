package com.test.zoom.controller;

import com.test.zoom.dto.dashboard.DashboardItemResponse;
import com.test.zoom.dto.dashboard.DashboardSettingItemResponse;
import com.test.zoom.dto.dashboard.DashboardSettingUpdateRequest;
import com.test.zoom.dto.dashboard.ExpenseChartItemResponse;
import com.test.zoom.service.dashboard.DashboardItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard/items")
@RequiredArgsConstructor
public class DashboardItemController {

    private final DashboardItemService dashboardItemService;


    /******** tag *******

    /** 화면 표시용 (특정 태그, visible=true 항목 + 오늘자 잔액) */
    @GetMapping
    public List<DashboardItemResponse> getVisibleItems() {
        return dashboardItemService.getVisibleItems();
    }

    /** 설정화면용 — 태그 구분 없이 전체 후보 계정을 한 번에 반환 (탭 분류는 프론트에서 처리) */
    @GetMapping("/settings")
    public List<DashboardSettingItemResponse> getAllSettings() {
        return dashboardItemService.getAllSettingsList();
    }

    /** 설정 저장 — 계정당 1행, 태그 포함 전체 upsert */
    @PutMapping("/settings")
    public ResponseEntity<Void> updateSettings(@Valid @RequestBody DashboardSettingUpdateRequest request) {
        dashboardItemService.updateSettings(request);
        return ResponseEntity.ok().build();
    }


    /******** chart *******

    /** 비용별 차트통계**/
    @GetMapping("/expense")
    public Map<String, List<ExpenseChartItemResponse>> getExpenseItems() {
        return dashboardItemService.getExpenseItems();
    }

 }