package com.test.zoom.dto.journal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 초기잔액 엑셀 업로드용 — 엑셀 1행 헤더가 그대로 필드명이 되는 기존 컨벤션(카드 업로드와 동일) 적용.
 * 엑셀 헤더: accountCode, accountName(참고용,미사용), category(참고용,미사용), amount, asOfDate, note(참고용,미사용)
 *
 * onExcelSave 계약: excelSaveToServer(parsed: T[], url)가 서버에 T[]를 보내고 T[]를 그대로 돌려받아
 * tableList에 identifierField 기준으로 병합한다. 이 화면에서는 accountCode를 identifierField로 사용한다.
 * → 서버는 받은 배열을 처리한 뒤, 결과(journalEntryId)를 채워서 같은 배열을 그대로 응답한다.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpeningBalanceRow {

    /** identifierField로 사용 (프론트 useTableManager 설정과 일치해야 함) */
    private String accountCode;
    
    /** 초기잔액등록 화면 사용자 편의용 */
    private String accountName;

    private BigDecimal amount;

    /** "yyyy-MM-dd" 또는 "yyyyMMdd" 둘 다 허용 (모든 행에 동일한 값이 반복 입력됨) */
    private String asOfDate;

    /** 처리 결과로 채워서 응답 — 생성된 개시분개 전표 ID (프론트 미리보기용, 요청 시엔 비워둬도 무방) */
    private Long journalEntryId;
}