package com.test.zoom.repository.card;


import com.test.zoom.entity.card.KBCardTransaction;
import com.test.zoom.entity.StatsProcedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface KBCardRepository extends JpaRepository<KBCardTransaction, Long> {

    List<KBCardTransaction> findAllByDeleted(boolean deleted);

    @Transactional
    @Modifying
    @Query("UPDATE SSCardTransaction c SET c.deleted = true WHERE c.id = :id")
    int markDeleted(@Param("id") Long id);

    @Query(value = "CALL getKBUsageTypeCurrencyStatsDate(:startDate, :endDate, :payDate)", nativeQuery = true)
    List<StatsProcedure> getKBUsageTypeCurrencyStats(LocalDate startDate, LocalDate endDate, LocalDate payDate);

    List<KBCardTransaction> findAllByDeletedFalseOrderByIdDesc();
}