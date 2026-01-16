package com.test.zoom.controller;

import com.test.zoom.dto.VoucherCreateRequest;
import com.test.zoom.service.VoucherService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/entry")
@AllArgsConstructor
public class EntryController {
    private final VoucherService voucherService;

    @PostMapping("/create")
    public ResponseEntity<Long> createVoucher( @RequestBody @Valid VoucherCreateRequest request ) {
        Long voucherId = voucherService.createVoucher(request);
        return ResponseEntity.ok(voucherId);
    }
}
