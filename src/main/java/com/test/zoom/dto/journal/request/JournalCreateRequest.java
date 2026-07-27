package com.test.zoom.dto.journal.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class JournalCreateRequest {

    /** 전표일자 */
    @NotNull
    private LocalDate entryDate;

    /** 적요 */
    private String description;

    /** 거래처 */
    private String vendor;

    /** 가족 태그 */
    private String memberTag;

    /** 분개라인 */
    @Valid
    @NotEmpty
    private List<JournalLineRequest> lines;

}