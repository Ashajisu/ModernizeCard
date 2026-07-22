package com.test.zoom.entity.card;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

/** 할부를 지원하는 카드사만 선택적으로 갖는 부가 정보 → 카드사 전용 화면에서만 사용 **/
@Embeddable
@Data
public class InstallmentInfo {

    @Column(name = "installment_total")
    private Long installmentTotal;

    @Column(name = "installment_months")
    private Integer installmentMonths;

    @Column(name = "installment_number")
    private Integer installmentNumber;

    @Column(name = "balance_after_deposit")
    private Long balanceAfterDeposit;
}