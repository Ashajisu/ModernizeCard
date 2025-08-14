package com.test.zoom.repository;

import com.test.zoom.entity.SHCardTransaction;
import com.test.zoom.entity.StatsProcedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SHCardRepository extends JpaRepository<SHCardTransaction, Long> {

    List<SHCardTransaction> findAllByDeleted(boolean deleted);

    @Transactional
    @Modifying
    @Query("UPDATE SHCardTransaction c SET c.deleted = true WHERE c.id = :id")
    int markDeleted(@Param("id") Long id);

    @Query(value = "CALL getSHUsageTypeCurrencyStats()", nativeQuery = true)
    List<StatsProcedure> getSHUsageTypeCurrencyStats();

}