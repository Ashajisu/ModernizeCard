package com.test.zoom.repository.card;

import com.test.zoom.entity.card.NHCardTransaction;
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
public interface NHCardRepository extends JpaRepository<NHCardTransaction, Long> {

    List<NHCardTransaction> findAllByDeleted(boolean deleted);

    @Transactional
    @Modifying
    @Query("UPDATE NHCardTransaction c SET c.deleted = true WHERE c.id = :id")
    int markDeleted(@Param("id") Long id);

    @Query(value = "CALL getNHUsageTypeCurrencyStatsDate(:startDate, :endDate, :payDate)", nativeQuery = true)
    List<StatsProcedure> getNHUsageTypeCurrencyStats(LocalDate startDate, LocalDate endDate, LocalDate payDate);

    List<NHCardTransaction> findAllByDeletedFalseOrderByIdDesc();
}
