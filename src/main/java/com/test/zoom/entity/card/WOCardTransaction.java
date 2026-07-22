package com.test.zoom.entity.card;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "woori_card")
public class WOCardTransaction extends CardTransaction {

    @Embedded
    private InstallmentInfo installmentInfo; // 삼성카드는 할부 지원 → 필드 추가
}
