package com.test.zoom.repository.journal;

import com.test.zoom.entity.journal.Account;
import com.test.zoom.entity.journal.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface JournalLineRepository extends JpaRepository<com.test.zoom.entity.journal.JournalLine, Long> {

    /**
     * 특정 카드미지급금 계정에 대해, 특정 결제일(payment_date)에 묶인 카드거래 분개들의 순수 부채 증감액.
     * (일반결제로 늘어난 부채) − (포인트결제로 이미 상계된 부채) = 이번 결제일에 실제로 계좌에서 빠져나가야 할 금액
     */
    @Query("SELECT COALESCE(SUM(l.creditAmount), 0) - COALESCE(SUM(l.debitAmount), 0) " +
           "FROM JournalLine l " +
           "WHERE l.account = :liabilityAccount " +
           "AND l.journalEntry.source = :source " +
           "AND l.journalEntry.paymentDate = :paymentDate " +
           "AND l.journalEntry.deleted = false")
    BigDecimal sumNetLiabilityByAccountAndPaymentDate(
            @Param("liabilityAccount") Account liabilityAccount,
            @Param("paymentDate") LocalDate paymentDate,
            @Param("source") JournalEntry.Source source);

    /**
     * 특정 시점까지 누적된 계정별 차변/대변 합계를 확정(confirmed) 여부로 나누어 집계.
     * 대차대조표(누적 잔액) 계산에 사용.
     * 결과 배열: [accountId, confirmed(Boolean), sumDebit, sumCredit]
     */
    @Query("SELECT l.account.id, l.journalEntry.confirmed, COALESCE(SUM(l.debitAmount),0), COALESCE(SUM(l.creditAmount),0) " +
           "FROM JournalLine l " +
           "WHERE l.journalEntry.entryDate <= :asOfDate AND l.journalEntry.deleted = false " +
           "GROUP BY l.account.id, l.journalEntry.confirmed")
    List<Object[]> aggregateBalancesAsOf(@Param("asOfDate") LocalDate asOfDate);

    /**
     * 특정 기간 내(entryDate BETWEEN from AND to) 계정별 차변/대변 합계.
     * 손익계산서(기간 손익) 계산에 사용.
     * 결과 배열: [accountId, sumDebit, sumCredit]
     */
    @Query("SELECT l.account.id, COALESCE(SUM(l.debitAmount),0), COALESCE(SUM(l.creditAmount),0) " +
           "FROM JournalLine l " +
           "WHERE l.journalEntry.entryDate BETWEEN :from AND :to AND l.journalEntry.deleted = false " +
           "GROUP BY l.account.id")
    List<Object[]> aggregateBalancesBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);
}
