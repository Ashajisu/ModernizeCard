package com.test.zoom.dto.journal.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 카드명세서 집계전표(비용확정) 생성 결과.
 * ⚠️ 이 전표는 이제 결제(SETTLEMENT)와 무관하므로, 연쇄삭제된 정산전표 목록 필드는 제거함.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardStatementRegenerateResponse {
    private Long entryId;
    private String cardCompanyCode;
}
