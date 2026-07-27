package com.test.zoom.entity.card;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "card_kookmin")
public class KBCardTransaction extends CardTransaction {
    
}
