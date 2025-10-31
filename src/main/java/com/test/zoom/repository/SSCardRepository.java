package com.test.zoom.repository;


import com.test.zoom.entity.SSCardTransaction;
import com.test.zoom.entity.StatsProcedure;
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
public interface SSCardRepository extends JpaRepository<SSCardTransaction, Long> {

    List<SSCardTransaction> findAllByDeleted(boolean deleted);

    @Transactional
    @Modifying
    @Query("UPDATE SSCardTransaction c SET c.deleted = true WHERE c.id = :id")
    int markDeleted(@Param("id") Long id);

    @Query(value = "CALL getSSUsageTypeCurrencyStatsDate(:startDate, :endDate, :payDate)", nativeQuery = true)
    List<StatsProcedure> getSSUsageTypeCurrencyStats(LocalDate startDate, LocalDate endDate, LocalDate payDate);

    List<SSCardTransaction> findAllByDeletedFalseOrderByIdDesc();
}