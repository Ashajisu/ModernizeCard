package com.test.zoom.entity.card;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "card_shinhan")
@Data
public class SHCardTransaction extends CardTransaction {
    // installmentInfo 없음 — 아예 컬럼 자체가 안 생김
}
