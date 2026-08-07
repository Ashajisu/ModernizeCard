package com.test.zoom.controller;

import com.test.zoom.dto.Search;
import com.test.zoom.dto.journal.request.JournalCreateRequest;

import com.test.zoom.dto.journal.request.JournalSearchRequest;
import com.test.zoom.dto.journal.request.JournalUpdateRequest;
import com.test.zoom.dto.journal.response.JournalDetailResponse;
import com.test.zoom.dto.journal.response.JournalResponse;
import com.test.zoom.entity.journal.JournalEntry;
import com.test.zoom.service.journal.JournalCommandService;
import com.test.zoom.service.journal.JournalQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/journal/entries")
@RequiredArgsConstructor
public class JournalController {
    
    private final JournalCommandService journalCommandService;
    private final JournalQueryService journalQueryService;

    /**
     * 전표 목록 조회
     */
    @PostMapping("/search/test")
    public ResponseEntity<Map<String, List<JournalEntry>>> searchTest(@RequestBody Search search) {
        log.debug("[/journal/search] search", search);
        search.normalizeLastMonth();
        List<JournalEntry> journals = journalQueryService.searchTest(search);
        log.debug("[/journal/search] list", Map.of("list", journals));
        return ResponseEntity.ok(Map.of("list", journals));
    }
    
    /** 전표 수기입력 */
    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody JournalCreateRequest request) {
        Long id = journalCommandService.create(request);
        return ResponseEntity.ok(id);
    }

    /** 전표 조회 (월별/계정별/금액별/날짜별/거래처별 — JournalSearchRequest 쿼리파라미터) */
    @GetMapping
    public List<JournalResponse> search(JournalSearchRequest request) {
        LocalDate from = request.getFromDate() != null
                ? request.getFromDate()
                : LocalDate.now().withDayOfMonth(1);
        request.setFromDate(from);

        LocalDate to = request.getToDate() != null
                ? request.getToDate()
                : LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        request.setToDate(to);
        
        return journalQueryService.search(request);
    }

    /** 전표 상세 (분개라인 포함) */
    @GetMapping("/{id}")
    public JournalDetailResponse getDetail(@PathVariable Long id) {
        return journalQueryService.getDetail(id);
    }

    /** 전표 수정 (수기입력 전표만 가능) */
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody JournalUpdateRequest request) {
        journalCommandService.update(id, request);
        return ResponseEntity.ok().build();
    }

    /** 전표 삭제 (소프트 삭제) */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        journalCommandService.delete(id);
        return ResponseEntity.ok().build();
    }

    /** 미확인 전표 확정 처리 */
    @PatchMapping("/{id}/confirm")
    public ResponseEntity<Void> confirm(@PathVariable Long id) {
        journalCommandService.confirm(id);
        return ResponseEntity.ok().build();
    }
}