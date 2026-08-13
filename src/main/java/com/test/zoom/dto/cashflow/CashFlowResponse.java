package com.test.zoom.dto.cashflow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashFlowResponse {
    private String yearMonth;
    private SalaryFlowResponse salaryFlow;
    private EmergencyFlowResponse emergencyFlow;
    private DutchPayFlowResponse dutchPayFlow;
    private UnassignedFlowResponse unassignedFlow; // 신규

    /** 급여 잔여금 + (비상금 사용액, 음수) + 더치페이 순현금(선지출-정산입금, 음수) */
    private BigDecimal totalCashFlow;
}
