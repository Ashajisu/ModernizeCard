package com.test.zoom.dto.journal.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountBalanceDto {
    private Long accountId;
    private String code;
    private String name;
    private Long parentId;
    private Integer depth;           // 트리 들여쓰기용 (0=최상위)
    private BigDecimal confirmedAmount;    // 확정된 전표만 반영한 금액
    private BigDecimal unconfirmedAmount;  // 미확인 전표 반영분 (참고용)
    private BigDecimal totalAmount;        // confirmed + unconfirmed
    private boolean isLeaf;          // 자식 계정이 없는 말단 계정 여부 (분개 직접 반영 대상)
}
