package com.test.zoom.service.journal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 매일 새벽 자동 분개 파이프라인 실행 진입점.
 * 순서: 1) 카드결제 1단계 → 2) 계좌출금 2단계(청구확정) → 3) 정기(적금/대출/고정지출) 분개
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JournalBatchScheduler {

    private final CardJournalGenerationService cardJournalGenerationService;
    private final SettlementBatchService settlementBatchService;
    private final RecurringJournalBatchService recurringJournalBatchService;

    //카드 화면에서 수동으로 호출하도록 수정할 예정
    /** 매일 04:00 실행 — cron: 초 분 시 일 월 요일 */
//    @Scheduled(cron = "0 9 13 * * *")
    public void runDailyBatch() {
        LocalDate today = LocalDate.now();
        log.info("=== 일일 자동분개 배치 시작: {} ===", today);

        // 최근 3일치 재조회 — 카드사 확정 반영 지연 대비
        LocalDateTime from = LocalDateTime.of(today.minusDays(3), LocalTime.MIN);
        LocalDateTime to = LocalDateTime.of(today, LocalTime.MAX);
        cardJournalGenerationService.generateForPeriod(from, to);

        settlementBatchService.generateForDate(today);
        recurringJournalBatchService.generateForDate(today);

        log.info("=== 일일 자동분개 배치 종료: {} ===", today);
    }
}
