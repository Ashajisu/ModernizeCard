package com.test.zoom.controller;

import com.test.zoom.entity.StatsProcedure;
import com.test.zoom.entity.card.SHCardTransaction;
import com.test.zoom.entity.entry.Account;
import com.test.zoom.repository.card.SHCardRepository;
import com.test.zoom.service.VoucherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/base")
public class SelectController {
    
    private final VoucherService voucherService;

    /** 선택 목록을 조회합니다. **/
    
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAccounts() {
        List<Account> list = voucherService.getAccounts();
        return ResponseEntity.ok(list);
    }
}
