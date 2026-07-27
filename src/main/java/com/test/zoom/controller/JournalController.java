package com.test.zoom.controller;

import com.test.zoom.dto.journal.request.JournalCreateRequest;
import com.test.zoom.dto.journal.request.JournalSearchRequest;
import com.test.zoom.dto.journal.request.JournalUpdateRequest;
import com.test.zoom.dto.journal.response.JournalDetailResponse;
import com.test.zoom.dto.journal.response.JournalResponse;
import com.test.zoom.service.journal.JournalCommandService;
import com.test.zoom.service.journal.JournalQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/journal")
@RequiredArgsConstructor
public class JournalController {

    private final JournalQueryService queryService;
    private final JournalCommandService commandService;

    /**
     * 전표 목록 조회
     */
    @GetMapping
    public Page<JournalResponse> search(
            JournalSearchRequest request
    ) {
        return queryService.search(request);
    }

    /**
     * 전표 상세 조회
     */
    @GetMapping("/{journalId}")
    public JournalDetailResponse detail(
            @PathVariable Long journalId
    ) {
        return queryService.findById(journalId);
    }

    /**
     * 전표 등록
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(
            @Valid @RequestBody JournalCreateRequest request
    ) {
        return commandService.create(request);
    }

    /**
     * 전표 수정
     */
    @PutMapping("/{journalId}")
    public void update(
            @PathVariable Long journalId,
            @Valid @RequestBody JournalUpdateRequest request
    ) {
        commandService.update(journalId, request);
    }

    /**
     * 전표 삭제
     */
    @DeleteMapping("/{journalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long journalId
    ) {
        commandService.delete(journalId);
    }

    /**
     * 전표 확정
     */
    @PatchMapping("/{journalId}/confirm")
    public void confirm(
            @PathVariable Long journalId
    ) {
        commandService.confirm(journalId);
    }

    /**
     * 전표 확정 취소
     */
    @PatchMapping("/{journalId}/unconfirm")
    public void unconfirm(
            @PathVariable Long journalId
    ) {
        commandService.unconfirm(journalId);
    }
}