package com.test.zoom.dto.journal.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class JournalLineRequest {

    /** 계정과목 */
    @NotNull
    private Long accountId;

    /** 차변금액 */
    @DecimalMin(value = "0.00")
    private BigDecimal debitAmount;

    /** 대변금액 */
    @DecimalMin(value = "0.00")
    private BigDecimal creditAmount;

    /** 라인 메모 */
    private String memo;

}