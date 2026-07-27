package com.test.zoom.dto.journal.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 전표 수정 요청.
 * 수기입력(MANUAL) 전표만 수정 가능 — 자동생성 전표(CARD_IMPORT/SETTLEMENT/RECURRING)는
 * 원본 데이터 재생성으로만 갱신되어야 하므로 서비스 단에서 source 검증 필요.
 * 라인은 전체 교체 방식(기존 라인 삭제 후 재생성)으로 처리한다.
 */
@Data
public class JournalUpdateRequest {

    @NotNull
    private LocalDate entryDate;

    private String description;

    private String vendor;

    private String memberTag;

    @Valid
    @NotEmpty
    private List<JournalLineRequest> lines;
}
