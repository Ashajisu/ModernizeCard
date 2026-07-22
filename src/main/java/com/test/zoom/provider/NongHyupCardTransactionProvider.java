package com.test.zoom.provider;

import com.test.zoom.entity.card.CardTransaction;
import com.test.zoom.repository.card.NHCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NongHyupCardTransactionProvider implements CardTransactionProvider {

    private final NHCardRepository repository;

    @Override
    public List<? extends CardTransaction> findConfirmedTransactions(LocalDateTime from, LocalDateTime to) {
        return repository.findByDeletedFalseAndTransactionDateBetween(from, to);
        // 모든 거래가 곧 확정 거래 — 별도 필터 없음
    }

    @Override
    public String getCardCompanyCode() {
        return "NONGHYUP";
    }
}