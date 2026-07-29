package com.test.zoom.dto.journal.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    private Long id;
    private String code;
    private String name;
    private String category;  // ASSET/LIABILITY/EQUITY/REVENUE/EXPENSE
    private Long parentId;
    private boolean leaf;     // 자식 계정이 없는 말단 계정 — 분개 입력 시 이것만 선택 가능하게 하는 용도
    private boolean active;
}