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
//    private BigDecimal salaryIncome;           // 이자 수입 (+)
    private List<CashFlowLineDto> variableLivingItems; // 변동생활비 하위 계정별 지출 (-)
    private BigDecimal usageTotal;                      // 비상금 사용 합계 (-)

//    private BigDecimal savingsOutflow;         // 저축/적금 (-)
//    private BigDecimal loanPrincipalOutflow;   // 대출 원금상환 (-)
//    private BigDecimal loanInterestOutflow;    // 대출 이자 (-)
//    private BigDecimal remainingCash;          // 비상금 잔여금 = 이자수입 - 비상금생활비 - 적금 - 원금 - 이자
}