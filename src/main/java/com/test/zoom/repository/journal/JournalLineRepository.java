package com.test.zoom.repository.journal;

import com.test.zoom.entity.journal.Account;
import com.test.zoom.entity.journal.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
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

    /**
     * 전표 ID 목록에 대해 각 전표의 대표 금액(차변 합계, 0보다 큰 것만)을 한번에 집계.
     * 전표조회(목록) 화면에서 entry.getLines() 지연로딩에 의존하지 않기 위해 사용
     * — N+1 문제와 트랜잭션 경계 밖 LazyInitializationException을 동시에 방지한다.
     * 결과 배열: [journalEntryId, sumDebitAmount]
     */
    @Query("SELECT l.journalEntry.id, COALESCE(SUM(l.debitAmount), 0) " +
            "FROM JournalLine l " +
            "WHERE l.journalEntry.id IN :entryIds AND l.debitAmount > 0 " +
            "GROUP BY l.journalEntry.id")
    List<Object[]> sumDebitByEntryIds(@Param("entryIds") List<Long> entryIds);

    /** 이 계정을 참조하는 분개라인이 하나라도 있는지 — 계정과목 카테고리 변경 가능 여부 판단용 */
    boolean existsByAccount_Id(Long accountId);

    /**
     * 은행거래 중복 검출용 — 같은 계좌, 같은 날짜, 같은 금액(차변 또는 대변)의 분개라인이
     * 이미 있는지 확인. 적요/거래처가 완전히 같은 경우까지는 요구하지 않고 날짜+금액만으로 1차 판정
     * (완전 자동 차단이 아니라 "의심" 표시이므로 최종 판단은 사용자가 화면에서 함).
     */
    @Query("SELECT COUNT(l) > 0 FROM JournalLine l " +
            "WHERE l.account.id = :accountId " +
            "AND l.journalEntry.entryDate = :entryDate " +
            "AND (l.debitAmount = :amount OR l.creditAmount = :amount) " +
            "AND l.journalEntry.deleted = false")
    boolean existsPossibleDuplicate(
            @Param("accountId") Long accountId,
            @Param("entryDate") LocalDate entryDate,
            @Param("amount") BigDecimal amount);


    /**
     * 현금흐름 조회용
     * **/
    @Query("""
    SELECT COALESCE(SUM(l.debitAmount), 0)
    FROM JournalLine l
    JOIN l.journalEntry e
    WHERE l.account.id IN :accountIds
      AND e.entryDate BETWEEN :from AND :to
      AND e.deleted = false
""")
    BigDecimal sumDebitAmountByAccountIds(
            @Param("accountIds") Collection<Long> accountIds,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to);
}
