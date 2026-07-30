package com.test.zoom.dto.journal;

/**
 * API 계약용 전표 출처 enum.
 * 엔티티 {@link com.test.zoom.entity.journal.JournalEntry.Source}를 DTO에서 직접 참조하지 않기 위해 분리.
 * 엔티티 구조가 바뀌어도 API 계약(JSON 응답 형태)은 영향받지 않는다.
 */
public enum JournalSource {
    MANUAL,
    CARD_IMPORT,
    SETTLEMENT,
    RECURRING,
    OPENING,
    BANK_IMPORT
}
