package com.test.zoom.dto.journal.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** "이용카드" select 옵션 — 카드사+usedCard를 하나로 묶어 프론트가 두 값을 한번에 선택하게 한다 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardOptionResponse {
    private String cardCompanyCode;
    private String label;
}
