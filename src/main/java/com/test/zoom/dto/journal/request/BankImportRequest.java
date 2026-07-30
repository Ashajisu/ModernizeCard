package com.test.zoom.dto.journal.request;

import com.test.zoom.dto.journal.BankImportRow;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class BankImportRequest {

    /** 대상 계좌(예: kb입출금) — 엑셀 전체가 이 계좌의 거래내역이라는 전제 */
    @NotNull
    private Long targetAccountId;

    @Valid
    @NotEmpty
    private List<BankImportRow> rows;
}
