package com.test.zoom.dto.journal.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class JournalLineResponse {

    private Long id;

    private Long accountId;

    private String accountCode;

    private String accountName;

    private BigDecimal debitAmount;

    private BigDecimal creditAmount;

    private String memo;

}