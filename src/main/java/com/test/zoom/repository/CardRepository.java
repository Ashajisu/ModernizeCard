package com.test.zoom.repository;

import com.test.zoom.entity.CardTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CardRepository extends JpaRepository<CardTransaction, Long> {
    List<CardTransaction> findAllByDeleted(boolean deleted);

    CardTransaction save(CardTransaction dto);

    @Transactional
    @Modifying
    @Query("UPDATE CardTransaction c SET c.deleted = true WHERE c.id = :id")
    int updateDeletedById(@Param("id") Long id);
}