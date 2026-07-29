package com.test.zoom.dto.journal.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/** 계정별 잔액 화면용 — 대차대조표와 달리 5개 분류(자산/부채/자본/수익/비용) 전체를 보여준다 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountBalanceResponse {
    private LocalDate asOfDate;
    private List<AccountBalanceDto> assets;
    private List<AccountBalanceDto> liabilities;
    private List<AccountBalanceDto> equity;
    private List<AccountBalanceDto> revenues;
    private List<AccountBalanceDto> expenses;
}
