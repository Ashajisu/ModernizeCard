package com.test.zoom.provider;

import com.test.zoom.entity.card.CardTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CardTransactionProvider {
    
    /** 전표 생성 대상 (확정된 거래만) */
    @Transactional(readOnly = true)
    List<? extends CardTransaction> findConfirmedTransactions(LocalDateTime from, LocalDateTime to);

    /**
     * 이 Provider가 곧 특정 카드사이므로(getCardCompanyCode()), 호출 전에 이미 카드사가 정해진 상태다.
     * paymentDate(정확일치) 기준 확정거래 조회 — 카드명세서 집계전표 생성 전용.
     * DB에서 직접 조건 조회하며, 거래일(transactionDate) 범위나 메모리 필터링에 의존하지 않는다.
     */
    @Transactional(readOnly = true)
    List<? extends CardTransaction> findConfirmedTransactionsByPaymentDate(LocalDate paymentDate);
    
    /** 이 Provider가 담당하는 카드사 코드 (목록 화면에서 card_company 표시용) */
    String getCardCompanyCode();
}