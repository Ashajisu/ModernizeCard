package com.test.zoom.dto.journal;

import com.test.zoom.dto.journal.response.AccountBalanceDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomeStatementResponse {
    private LocalDate fromDate;
    private LocalDate toDate;

    private List<AccountBalanceDto> revenues;   // 수익 계정 (현재는 분개 소스 없어 0으로 표시될 수 있음)
    private List<AccountBalanceDto> expenses;   // 비용 계정

    private BigDecimal totalRevenue;
    private BigDecimal totalExpense;
    private BigDecimal netIncome;   // totalRevenue - totalExpense
}
