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

    private String companyDisplayName(String code) {
        return switch (code) {
            case "KOOKMIN" -> "국민카드";
            case "SAMSUNG" -> "삼성카드";
            case "SHINHAN" -> "신한카드";
            case "WOORI" -> "우리카드";
            case "NONGHYUP" -> "농협카드";
            case "HYUNDAI" -> "현대카드";
            default -> code;
        };
    }

    /** "이용카드" select 옵션 구성용 — 모든 Provider의 distinct usedCard를 카드사와 묶어 반환 */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<com.test.zoom.dto.journal.response.CardOptionResponse> getCardOptions() {
        List<com.test.zoom.dto.journal.response.CardOptionResponse> options = new java.util.ArrayList<>();
        String companyLabel;
        for (CardTransactionProvider provider : providers) {
            companyLabel = companyDisplayName(provider.getCardCompanyCode());
            options.add(new com.test.zoom.dto.journal.response.CardOptionResponse(provider.getCardCompanyCode(), companyLabel));
        }
        return options;
    }
}