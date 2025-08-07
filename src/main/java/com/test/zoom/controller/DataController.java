package com.test.zoom.controller;

import com.test.zoom.entity.CardTransaction;

import com.test.zoom.repository.CardRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**controller 어노테이션에 의해 스프링 빈으로 등록 되어야, 컴포넌트 스캔의 대상이 되어 value 어노테이션이 동작한다.**/
@Slf4j
@RestController
@RequestMapping("/card")
@CrossOrigin(origins = "http://localhost:5173")
public class DataController {

    @Autowired
    private CardRepository cardRepository;

    /**사용자의 카드내역을 나열합니다. **/
    @GetMapping("/list")
    public ResponseEntity<Map <String, List<CardTransaction>>> getCardTransactionList() {
        List<CardTransaction> list = cardRepository.findAll();
        return ResponseEntity.ok(Map.of("list", list));
    }

    /**신규 카드내역을 저장합니다. **/
    @PostMapping("/save")
    public ResponseEntity<CardTransaction> saveTransaction(@RequestBody CardTransaction dto) {
        CardTransaction saved = cardRepository.save(dto);
        return ResponseEntity.ok(saved);
    }
}
