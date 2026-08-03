package com.test.zoom.controller;

import com.test.zoom.dto.journal.response.CardStatementRegenerateResponse;
import com.test.zoom.service.journal.CardStatementJournalService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/journal/card-statement")
@RequiredArgsConstructor
public class CardStatementJournalController {

    private final CardStatementJournalService cardStatementJournalService;

    /**
     * 카드사(companyCode)+실제 결제일(paymentDate) 기준 집계전표 (재)생성.
     * 이미 이 결제일 기준으로 SETTLEMENT(계좌출금) 전표가 있으면, confirmCascade=true 없이는
     * 409(IllegalStateException)로 안내만 하고 아무것도 지우지 않는다.
     * POST /journal/card-statement/regenerate?companyCode=SAMSUNG&paymentDate=2026-08-12&confirmCascade=false
     */
    @PostMapping("/regenerate")
    public CardStatementRegenerateResponse regenerate(
            @RequestParam String companyCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate paymentDate,
            @RequestParam(defaultValue = "false") boolean confirmCascade) {
        return cardStatementJournalService.regenerate(companyCode, paymentDate, confirmCascade);
    }
}
