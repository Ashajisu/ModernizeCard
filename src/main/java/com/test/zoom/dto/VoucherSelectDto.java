package com.test.zoom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//DTO에서 안전한 타입 : String, java.sql.Date, Long, BigDecimal
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoucherSelectDto {

    private Long id;
    private java.sql.Date voucherDate; //타입주의
    private String voucherType; //타입주의
    private String description;

    private String debitAccount;
    private String creditAccount;

    private Long amount;
}
