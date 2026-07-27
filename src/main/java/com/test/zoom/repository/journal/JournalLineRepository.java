package com.test.zoom.repository.journal;

import com.test.zoom.entity.journal.Account;
import com.test.zoom.entity.journal.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

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
}
