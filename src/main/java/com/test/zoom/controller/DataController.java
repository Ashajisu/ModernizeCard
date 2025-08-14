package com.test.zoom.controller;

import com.test.zoom.entity.StatsProcedure;
import com.test.zoom.entity.CardTransaction;

import com.test.zoom.repository.CardRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
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
        List<CardTransaction> list = cardRepository.findAllByDeleted(false);
        return ResponseEntity.ok(Map.of("list", list));
    }

    /**신규 카드내역을 저장합니다. **/
    @PostMapping("/save")
    public ResponseEntity<CardTransaction> saveTransaction(@RequestBody CardTransaction dto) {
        CardTransaction saved = cardRepository.save(dto);
        return ResponseEntity.ok(saved);
    }

    /**신규 카드내역을 논리 삭제합니다. **/
    @PostMapping("/delete")
    public ResponseEntity<CardTransaction> deleteTransaction(@RequestBody CardTransaction dto) {
        int deletedCnt = cardRepository.updateDeletedById(dto.getId());
        if(deletedCnt > 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok().build() ;
    }

    /**카드내역 목록을 저장합니다. **/
    @PostMapping("/saveList")
    public ResponseEntity<List<CardTransaction>> saveTransaction(@RequestBody List<CardTransaction> dto) {
        //엑셀 역순으로 저장.
        List<CardTransaction> reversedList = new ArrayList<>(dto);
        Collections.reverse(reversedList);
        List<CardTransaction> saved = cardRepository.saveAll(reversedList);
        return ResponseEntity.ok(saved);
    }

    /**이용구분별 통계를 조회합니다. **/
    @GetMapping("/usageTypeStats")
    public ResponseEntity<Map <String, List<StatsProcedure>>> getUsageTypeCurrencyStats() {
        List<StatsProcedure> stats = cardRepository.getUsageTypeCurrencyStats();
        System.out.println(stats);
        return ResponseEntity.ok(Map.of("list", stats));
    }
}
