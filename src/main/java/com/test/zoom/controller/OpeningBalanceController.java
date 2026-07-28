package com.test.zoom.controller;

import com.test.zoom.dto.journal.OpeningBalanceRow;
import com.test.zoom.service.journal.OpeningBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/journal/opening-balance")
@RequiredArgsConstructor
public class OpeningBalanceController {

    private final OpeningBalanceService openingBalanceService;

    /**
     * onExcelSave 계약: List<OpeningBalanceRow>를 받아 List<OpeningBalanceRow>를 그대로 반환한다
     * (excelSaveToServer가 응답을 T[]로 기대하고 tableList에 accountCode 기준으로 병합함).
     * 재업로드 시 기존 개시분개는 자동으로 대체됨.
     */
    @PostMapping("/upload")
    public List<OpeningBalanceRow> upload(@RequestBody List<OpeningBalanceRow> rows) {
        return openingBalanceService.importRows(rows);
    }
    
    @GetMapping
    public List<OpeningBalanceRow> list() {
        return openingBalanceService.findAll();
    }
}
