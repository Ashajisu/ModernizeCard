package com.test.zoom.service;

import com.test.zoom.provider.CardTransactionProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.test.zoom.entity.card.CardTransaction;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardTransactionAggregationService {

    private final List<CardTransactionProvider> providers; // Spring이 구현체 전부 자동 수집

    public List<CardTransaction> findAllForVoucher(LocalDateTime from, LocalDateTime to) {
        return providers.stream()
                .flatMap(p -> p.findConfirmedTransactions(from, to).stream())
                .map(tx -> (CardTransaction) tx)
                .sorted(Comparator.comparing(CardTransaction::getTransactionDate))
                .toList();
    }
}