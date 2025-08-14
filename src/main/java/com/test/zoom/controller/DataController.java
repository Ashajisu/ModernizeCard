package com.test.zoom.controller;

import com.test.zoom.entity.SSCardTransaction;
import com.test.zoom.entity.StatsProcedure;
import com.test.zoom.entity.SHCardTransaction;

import com.test.zoom.repository.SHCardRepository;
import com.test.zoom.repository.SSCardRepository;
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
    private SHCardRepository ShinHanCardR;

    @Autowired
    private SSCardRepository SamSungCardR;

    /**사용자의 카드내역을 나열합니다. **/
    @GetMapping("/list")
    public ResponseEntity<Map <String, List<SHCardTransaction>>> getCardTransactionList() {
        List<SHCardTransaction> list = ShinHanCardR.findAllByDeleted(false);
        return ResponseEntity.ok(Map.of("list", list));
    }

    /**신규 카드내역을 저장합니다. **/
    @PostMapping("/save")
    public ResponseEntity<SHCardTransaction> saveTransaction(@RequestBody SHCardTransaction dto) {
        SHCardTransaction saved = ShinHanCardR.save(dto);
        return ResponseEntity.ok(saved);
    }

    /**신규 카드내역을 논리 삭제합니다. **/
    @PostMapping("/delete")
    public ResponseEntity<SHCardTransaction> deleteTransaction(@RequestBody SHCardTransaction dto) {
        int deletedCnt = ShinHanCardR.markDeleted(dto.getId());
        if(deletedCnt > 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok().build() ;
    }

    /**카드내역 목록을 저장합니다. **/
    @PostMapping("/saveList")
    public ResponseEntity<List<SHCardTransaction>> saveTransaction(@RequestBody List<SHCardTransaction> dto) {
        //엑셀 역순으로 저장.
        List<SHCardTransaction> reversedList = new ArrayList<>(dto);
        Collections.reverse(reversedList);
        List<SHCardTransaction> saved = ShinHanCardR.saveAll(reversedList);
        return ResponseEntity.ok(saved);
    }

    /**이용구분별 통계를 조회합니다. **/
    @GetMapping("/usageTypeStats")
    public ResponseEntity<Map <String, List<StatsProcedure>>> getUsageTypeCurrencyStats() {
        List<StatsProcedure> stats = ShinHanCardR.getSHUsageTypeCurrencyStats();
        System.out.println(stats);
        return ResponseEntity.ok(Map.of("list", stats));
    }

    ///
    /**사용자의 카드내역을 나열합니다. **/
    @GetMapping("/list/samsung")
    public ResponseEntity<Map <String, List<SSCardTransaction>>> getSSCardTransactionList() {
        List<SSCardTransaction> list = SamSungCardR.findAllByDeleted(false);
        return ResponseEntity.ok(Map.of("list", list));
    }
}
