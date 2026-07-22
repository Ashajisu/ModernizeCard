package com.test.zoom.provider;

import com.test.zoom.entity.card.CardTransaction;

import java.time.LocalDateTime;
import java.util.List;

public interface CardTransactionProvider {
    /** 전표 생성 대상 (확정된 거래만) */
    List<? extends CardTransaction> findConfirmedTransactions(LocalDateTime from, LocalDateTime to);

    /** 이 Provider가 담당하는 카드사 코드 (목록 화면에서 card_company 표시용) */
    String getCardCompanyCode();
}