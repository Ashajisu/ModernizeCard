package com.test.zoom.dto.journal.request;

import com.test.zoom.dto.journal.JournalSource;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 전표 조회 검색 조건. 모든 필드는 선택(null 허용) — 채워진 조건만 AND로 결합된다.
 */
@Data
public class JournalSearchRequest {

    /** 조회 시작일 (entryDate 기준) */
    private LocalDate fromDate;

    /** 조회 종료일 (entryDate 기준) */
    private LocalDate toDate;

    /** 계정과목 ID — 해당 계정이 차변/대변 어느 쪽으로든 포함된 전표 */
    private Long accountId;

    /** 거래처명 (부분일치) */
    private String vendor;

    /** 가족구성원 태그 */
    private String memberTag;

    /** 전표 출처 */
    private JournalSource source;

    /** 확정여부 (null이면 전체) */
    private Boolean confirmed;

    /** 전표 금액(차변합계) 최소값 */
    private BigDecimal minAmount;

    /** 전표 금액(차변합계) 최대값 */
    private BigDecimal maxAmount;

    /** 페이지 (0-base) */
    private Integer page = 0;

    /** 페이지 크기 */
    private Integer size = 20;
}
