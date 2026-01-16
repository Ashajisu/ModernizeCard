package com.test.zoom.dto;

import com.test.zoom.entity.entry.VoucherType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class VoucherCreateRequest {
    @NotNull
    private LocalDate voucherDate;

    @NotNull
    private VoucherType voucherType; // TRANSFER, CARD, CASH, ADJUST

    @NotNull
    private Long debitAccountId; // 차변계정. 자산/비용 증가

    @NotNull
    private Long creditAccountId;   // 대변계정. 부채/자본/수익 증가

    @Positive
    private Long amount;

    private String description;
}
