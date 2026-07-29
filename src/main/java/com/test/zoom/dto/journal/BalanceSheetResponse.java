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
public class BalanceSheetResponse {
    private LocalDate asOfDate;

    private List<AccountBalanceDto> assets;
    private List<AccountBalanceDto> liabilities;

    private BigDecimal totalAssets;
    private BigDecimal totalLiabilities;
    private BigDecimal equity;          // 순자산 = totalAssets - totalLiabilities (자동계산, 플러그)

    private BigDecimal totalAssetsConfirmedOnly;
    private BigDecimal totalLiabilitiesConfirmedOnly;
    private BigDecimal equityConfirmedOnly;

    private boolean balanced;           // totalAssets == totalLiabilities + equity (항상 true — 검증용)
}
