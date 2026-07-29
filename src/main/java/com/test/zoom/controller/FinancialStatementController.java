package com.test.zoom.controller;

import com.test.zoom.dto.journal.BalanceSheetResponse;
import com.test.zoom.dto.journal.IncomeStatementResponse;
import com.test.zoom.dto.journal.response.AccountBalanceResponse;
import com.test.zoom.service.journal.AccountBalanceService;
import com.test.zoom.service.journal.BalanceSheetService;
import com.test.zoom.service.journal.IncomeStatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.YearMonth;

@RestController
@RequestMapping("/journal")
@RequiredArgsConstructor
public class FinancialStatementController {

    private final BalanceSheetService balanceSheetService;
    private final IncomeStatementService incomeStatementService;
    private final AccountBalanceService accountBalanceService;

    /** GET /journal/balance-sheet?asOfDate=2026-07-31 (생략 시 오늘 기준) */
    @GetMapping("/balance-sheet")
    public BalanceSheetResponse balanceSheet(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOfDate) {
        LocalDate target = asOfDate != null ? asOfDate : LocalDate.now();
        return balanceSheetService.generate(target);
    }

    /** GET /journal/income-statement?yearMonth=2026-07 (생략 시 이번달) */
    @GetMapping("/income-statement")
    public IncomeStatementResponse incomeStatement(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth) {
        YearMonth target = yearMonth != null ? yearMonth : YearMonth.now();
        return incomeStatementService.generate(target.atDay(1), target.atEndOfMonth());
    }

    /** GET /journal/income-statement/range?from=2026-01-01&to=2026-07-31 (연간/커스텀 기간 조회용) */
    @GetMapping("/income-statement/range")
    public IncomeStatementResponse incomeStatementRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return incomeStatementService.generate(from, to);
    }

    /** GET /journal/account-balance?asOfDate=2026-07-31 (생략 시 오늘 기준) — 5개 분류 전체 계정 잔액 */
    @GetMapping("/account-balance")
    public AccountBalanceResponse accountBalance(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOfDate) {
        LocalDate target = asOfDate != null ? asOfDate : LocalDate.now();
        return accountBalanceService.generate(target);
    }
}
