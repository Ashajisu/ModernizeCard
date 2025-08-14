package com.test.zoom.repository;

import com.test.zoom.entity.StatsProcedure;
import com.test.zoom.entity.SHCardTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CardRepository extends JpaRepository<SHCardTransaction, Long> {
    List<SHCardTransaction> findAllByDeleted(boolean deleted);

    SHCardTransaction save(SHCardTransaction dto);

    @Transactional
    @Modifying
    @Query("UPDATE SHCardTransaction c SET c.deleted = true WHERE c.id = :id")
    int updateDeletedById(@Param("id") Long id);

    @Query(value = "CALL getUsageTypeCurrencyStats()", nativeQuery = true)
    List<StatsProcedure> getUsageTypeCurrencyStats();
}