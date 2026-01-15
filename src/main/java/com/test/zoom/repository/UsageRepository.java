package com.test.zoom.repository;

import com.test.zoom.entity.UsageTransaction;
import com.test.zoom.entity.StatsProcedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
/** 전체 지출 view 조회 **/
public interface UsageRepository extends JpaRepository<UsageTransaction, Long> {

    @Query(value = "CALL getAllUsageTypeCurrencyStatsDate(:startDate, :endDate)", nativeQuery = true)
    List<StatsProcedure> getAllUsageTypeCurrencyStatsDate(LocalDate startDate, LocalDate endDate);

    List<UsageTransaction> findAllByOrderByCardCompanyAscIdDesc();
}