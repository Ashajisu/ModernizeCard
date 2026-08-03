package com.test.zoom.dto.journal.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardStatementRegenerateResponse {
    private Long entryId;
    private String cardCompanyCode;
    /** 이번 재생성 과정에서 함께 소프트삭제된 SETTLEMENT 전표들의 결제일 목록 (없으면 빈 리스트) */
    private List<LocalDate> deletedSettlementPaymentDates;
}
