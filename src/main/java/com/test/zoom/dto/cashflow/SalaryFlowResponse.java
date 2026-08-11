package com.test.zoom.dto.cashflow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryFlowResponse {
    private BigDecimal salaryIncome;           // 급여 수입 (+)
    private List<CashFlowLineDto> fixedLivingItems; // 고정생활비 하위 계정별 지출 (-)
    private BigDecimal fixedLivingTotal;
    private BigDecimal savingsOutflow;         // 저축/적금 (-)
    private BigDecimal loanPrincipalOutflow;   // 대출 원금상환 (-)
    private BigDecimal loanInterestOutflow;    // 대출 이자 (-)
    private BigDecimal remainingCash;          // 급여 잔여금 = 급여잔액 - 고정생활비 - 적금 - 원금 - 이자
}
