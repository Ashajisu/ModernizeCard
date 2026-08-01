package com.test.zoom.repository.card;


import com.test.zoom.entity.StatsProcedure;
import com.test.zoom.entity.card.CardTransaction;
import com.test.zoom.entity.card.HDCardTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HDCardRepository extends JpaRepository<HDCardTransaction, Long> {

    List<HDCardTransaction> findAllByDeleted(boolean deleted);

    @Transactional
    @Modifying
    @Query("UPDATE HDCardTransaction c SET c.deleted = true WHERE c.id = :id")
    int markDeleted(@Param("id") Long id);

    @Query(value = "CALL getHDUsageTypeCurrencyStatsDate(:startDate, :endDate, :payDate)", nativeQuery = true)
    List<StatsProcedure> getHDUsageTypeCurrencyStats(LocalDate startDate, LocalDate endDate, LocalDate payDate);

    List<HDCardTransaction> findAllByDeletedFalseOrderByIdDesc();

    List<? extends CardTransaction> findByDeletedFalseAndTransactionDateBetween(LocalDateTime from, LocalDateTime to);
}