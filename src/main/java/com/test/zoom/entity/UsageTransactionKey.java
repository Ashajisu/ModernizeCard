package com.test.zoom.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsageTransactionKey implements Serializable {
    private Long id;
    private String cardCompany;

    // equals & hashCode 필수
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsageTransactionKey)) return false;
        UsageTransactionKey that = (UsageTransactionKey) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(cardCompany, that.cardCompany);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cardCompany);
    }
}