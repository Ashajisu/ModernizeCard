package com.test.zoom.dto.cashflow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DutchPayFlowResponse {
    private BigDecimal prepaidAmount;    // 선지출 (양수로 표시)
    private BigDecimal settledAmount;    // 정산 입금 (양수로 표시)
    private BigDecimal unsettledAmount;  // 미정산 = 선지출 - 정산입금
}
