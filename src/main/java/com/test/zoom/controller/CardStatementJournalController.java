package com.test.zoom.controller;

import com.test.zoom.dto.journal.response.CardStatementRegenerateResponse;
import com.test.zoom.service.journal.CardStatementJournalService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("/journal/card-statement")
@RequiredArgsConstructor
public class CardStatementJournalController {

    private final CardStatementJournalService cardStatementJournalService;

    /**
     * 카드사(cardCompanyCode) + 대상월(yearMonth) 기준 "비용확정" 집계전표 (재)생성.
     * transactionDate가 해당 월에 속하는 거래를 전부 모아 집계한다 (결제일과 무관).
     *
     * POST /journal/card-statement/regenerate?cardCompanyCode=SHINHAN&yearMonth=2026-08
     */
    @PostMapping("/regenerate")
    public CardStatementRegenerateResponse regenerate(
            @RequestParam String cardCompanyCode,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth) {
        return cardStatementJournalService.regenerate(cardCompanyCode, yearMonth);
    }
}
