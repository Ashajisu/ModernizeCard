package com.test.zoom.service;

import com.test.zoom.entity.card.CardTransaction;
import com.test.zoom.entity.card.ExpenseListItem;
import com.test.zoom.provider.CardTransactionProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseManagementService {

    private final List<CardTransactionProvider> providers;

    public List<ExpenseListItem> findAllExpenses(LocalDateTime from, LocalDateTime to) {
        return providers.stream()
                .flatMap(p -> p.findConfirmedTransactions(from, to).stream()
                        .map(tx -> new ExpenseListItem((CardTransaction) tx, p.getCardCompanyCode())))
                .sorted(Comparator.comparing(ExpenseListItem::transactionDate))
                .toList();
    }
}