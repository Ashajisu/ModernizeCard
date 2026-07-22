package com.test.zoom.entity.card;

import java.time.LocalDateTime;

public record ExpenseListItem(
        String cardCompany,
        Long id,
        LocalDateTime transactionDate,
        LocalDateTime paymentDate,
        String usageType,
        Long amount,
        String usedCard,
        String merchantName,
        String benefitType,
        Long benefitAmount,
        String purchaseType,
        Long currency
) {
    public ExpenseListItem(CardTransaction tx, String cardCompany) {
        this(cardCompany, tx.getId(), tx.getTransactionDate(), tx.getPaymentDate(),
                tx.getUsageType(), tx.getAmount(), tx.getUsedCard(), tx.getMerchantName(),
                tx.getBenefitType(), tx.getBenefitAmount(), tx.getPurchaseType(), tx.getCurrency());
    }
}