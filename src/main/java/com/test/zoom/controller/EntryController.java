package com.test.zoom.controller;

import com.test.zoom.dto.VoucherCreateRequest;
import com.test.zoom.entity.entry.Account;
import com.test.zoom.service.VoucherService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/entry")
@AllArgsConstructor
public class EntryController {
    private final VoucherService voucherService;
    
    /** 계정관리 */
    
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAccounts() {
        List<Account> list = voucherService.getAccounts();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/account/delete")
    public ResponseEntity<Account> deleteAccount( @RequestBody Account account) {
        Account deleted = voucherService.deleteAccount(account);
        return ResponseEntity.ok(deleted);
    }

    @PostMapping("/account/save")
    public ResponseEntity<Account> saveAccount( @RequestBody Account account) {
        Account updated = voucherService.saveAccount(account);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/accounts/save")
    public ResponseEntity<List<Account>> saveAccounts( @RequestBody List<Account> account) {
        List<Account> reversedList = new ArrayList<>(account);
        Collections.reverse(reversedList);
        List<Account> updated = voucherService.saveAccounts(reversedList);
        return ResponseEntity.ok(updated);
    }

    /** 전표 관리 */

    @PostMapping("/voucher/create")
    public ResponseEntity<Long> createVoucher( @RequestBody @Valid VoucherCreateRequest request ) {
        Long voucherId = voucherService.createVoucher(request);
        return ResponseEntity.ok(voucherId);
    }
    
    
}
