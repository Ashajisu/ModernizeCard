package com.test.zoom.dto.cashflow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyFlowResponse {
    private List<CashFlowLineDto> variableLivingItems; // 비상금 생활비 계정별 지출 (-) — 기존 필드명 유지
    private BigDecimal usageTotal;                      // 비상금 생활비 합계 (-) — 기존 필드명 유지

    // 아래는 AccountPurposeTag 전환에 따라 신규 추가된 필드
    private BigDecimal interestIncome;      // 이자수익 (+)
    private BigDecimal savingsOutflow;      // 저축/적금 (-)
    private BigDecimal loanRepaymentOutflow; // 대출상환(학자금) (-)
    private BigDecimal interestOutflow;     // 이자상환 (-)
    private BigDecimal netBalanceChange;    // 비상금 잔액 변화 = 이자수익 - 생활비 - 저축 - 대출상환 - 이자
}
