package com.test.zoom.repository;

import com.test.zoom.entity.StatsProcedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TotalRepository extends JpaRepository<StatsProcedure, Long> {

    @Query(value = "CALL getMonthlyStatsAll()", nativeQuery = true)
    String getChart1UsageTypeCurrencyStats();
}