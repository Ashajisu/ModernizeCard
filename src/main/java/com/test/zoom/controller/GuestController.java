package com.test.zoom.controller;

import com.test.zoom.entity.*;
import com.test.zoom.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/guest")
public class GuestController {

    @Autowired
    private SHCardRepository ShinHanCardR;

    /// shinhan : SH
    /**사용자의 카드내역을 나열합니다. **/
    @GetMapping("/list")
    public ResponseEntity<Map <String, List<SHCardTransaction>>> getCardTransactionList() {
        List<SHCardTransaction> list = ShinHanCardR.findAllByDeleted(false);
        return ResponseEntity.ok(Map.of("list", list));
    }

    /**이용구분별 통계를 조회합니다. **/
    @GetMapping("/usageTypeStats")
    public ResponseEntity<Map <String, List<StatsProcedure>>> getUsageTypeCurrencyStats() {
        List<StatsProcedure> stats = ShinHanCardR.getSHUsageTypeCurrencyStats();
        System.out.println(stats);
        return ResponseEntity.ok(Map.of("list", stats));
    }
}
