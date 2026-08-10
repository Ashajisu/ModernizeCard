package com.test.zoom.dto.journal.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/** 예산 설정(편집) 다이얼로그용 — 전체 EXPENSE leaf 계정 + 현재 설정된 예산액(없으면 0) */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetEditItemResponse {
    private Long accountId;
    private String accountCode;
    private String accountName;
    private BigDecimal budgetAmount;
}
