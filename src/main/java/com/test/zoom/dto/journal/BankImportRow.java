package com.test.zoom.dto.journal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 은행 거래내역 엑셀 업로드 — 파싱된 원본 행이자 미리보기/저장 요청에 공용으로 쓰이는 DTO.
 * 엑셀 헤더(1행) 컨벤션: transactionDate, description, deposit, withdrawal, rawAccountInfo(참고용,미사용)
 *
 * 흐름: parseExcel로 클라이언트에서 파싱 → /preview 로 전송(추천 계정/중복여부 채워서 응답)
 *      → 사용자가 accountId/skip 수정 → /save 로 최종 전송
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankImportRow {

    /** "yyyy-MM-dd" 또는 "yyyyMMdd" */
    private String transactionDate;

    /** 적요 */
    private String description;

    /** 입금액 (없으면 0) */
    private BigDecimal deposit;

    /** 출금액 (없으면 0) */
    private BigDecimal withdrawal;

    /** 원본 거래계좌 표기 (참고용, 서버 로직에서는 사용 안 함) */
    private String rawAccountInfo;

    /**
     * 엑셀에서 사용자가 미리 지정한 상대계정 코드 (선택 입력).
     * 우선순위: 이 값(존재+유효) > MerchantCategoryResolver 추천 > 미분류
     */
    private String accountCode;

    /** 상대계정 — 서버가 추천값을 채워주고, 사용자가 화면에서 수정 가능 */
    private Long accountId;

    /** 상대계정명 (참고 표시용) */
    private String accountName;

    /** 기존 전표와 중복 의심 여부 (미리보기 시 서버가 판정) */
    private boolean duplicate;

    /** 사용자가 저장 대상에서 제외하기로 한 경우 true (중복 등의 이유로) */
    private boolean skip;
}