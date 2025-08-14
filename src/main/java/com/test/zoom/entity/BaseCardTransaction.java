package com.test.zoom.entity;

import java.time.LocalDateTime;

public interface BaseCardTransaction {

    Long getId();
    LocalDateTime getTransactionDate();
    String getUsageType();
    Long getCurrency();
    boolean isDeleted();
    // 필요한 공통 필드 getter
}
