package com.test.zoom.repository;


import com.test.zoom.entity.StatsProcedure;
import com.test.zoom.entity.WOCardTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WOCardRepository extends JpaRepository<WOCardTransaction, Long> {

    List<WOCardTransaction> findAllByDeleted(boolean deleted);

    @Transactional
    @Modifying
    @Query("UPDATE WOCardTransaction c SET c.deleted = true WHERE c.id = :id")
    int markDeleted(@Param("id") Long id);

    @Query(value = "CALL getWOUsageTypeCurrencyStatsDate(:startDate, :endDate, :payDate)", nativeQuery = true)
    List<StatsProcedure> getWOUsageTypeCurrencyStats(LocalDate startDate, LocalDate endDate, LocalDate payDate);

    List<WOCardTransaction> findAllByDeletedFalseOrderByIdDesc();
}