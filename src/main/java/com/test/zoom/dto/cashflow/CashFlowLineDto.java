package com.test.zoom.dto.cashflow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashFlowLineDto {
    private String accountCode;
    private String accountName;
    /** 부호 있는 금액 (유출은 음수, 유입은 양수) */
    private BigDecimal amount;
}
