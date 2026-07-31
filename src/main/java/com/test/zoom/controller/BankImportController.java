package com.test.zoom.controller;

import com.test.zoom.dto.journal.BankImportRow;
import com.test.zoom.dto.journal.request.BankImportRequest;
import com.test.zoom.dto.journal.response.BankImportResult;
import com.test.zoom.service.journal.BankImportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/journal/entries/bank-import")
@RequiredArgsConstructor
public class BankImportController {

    private final BankImportService bankImportService;

    /** 계정 추천 + 중복검출만 수행 (아무것도 저장하지 않음) */
    @PostMapping("/preview")
    public List<BankImportRow> preview(@Valid @RequestBody BankImportRequest request) {
        return bankImportService.preview(request);
    }

    /** 사용자가 확정한 내용으로 실제 전표 생성 */
    @PostMapping("/save")
    public BankImportResult save(@Valid @RequestBody BankImportRequest request) {
        return bankImportService.save(request);
    }
}
