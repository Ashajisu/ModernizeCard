package com.test.zoom.repository;

import com.test.zoom.entity.CardTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<CardTransaction, Long> {
    List<CardTransaction> findAll();
}