package com.test.zoom.dto.journal.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class JournalLineRequest {

    /** 계정과목 ID */
    @NotNull
    private Long accountId;

    /** 차변금액 (없으면 0) */
    private BigDecimal debitAmount;

    /** 대변금액 (없으면 0) */
    private BigDecimal creditAmount;

    /** 라인 메모 */
    private String memo;

}