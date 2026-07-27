package com.test.zoom.service.journal;

import com.test.zoom.entity.card.CardTransaction;
import com.test.zoom.entity.journal.Account;
import com.test.zoom.entity.journal.JournalEntry;
import com.test.zoom.entity.journal.MerchantCategoryRule;
import com.test.zoom.provider.CardTransactionProvider;
import com.test.zoom.repository.journal.AccountRepository;
import com.test.zoom.repository.journal.JournalEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 1단계 분개: 카드결제 확정거래(findConfirmedTransactions) → 비용/카드미지급금 분개 자동 생성.
 * 포인트 결제(POINT_SETTLEMENT) 매칭 시 비용 계정 없이 부채-자산 상계로 처리.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CardJournalGenerationService {

    /** CardTransactionProvider.getCardCompanyCode() → 카드미지급금 계정 코드 */
    private static final Map<String, String> LIABILITY_ACCOUNT_CODE = Map.of(
            "KOOKMIN", "2000",
            "SAMSUNG", "2001",
            "SHINHAN", "2002",
            "WOORI", "2003",
            "NONGHYUP", "2004"
    );

    private static final String UNCLASSIFIED_ACCOUNT_CODE = "9999";
    private static final String CARD_POINT_ACCOUNT_CODE = "1100";

    private final List<CardTransactionProvider> providers;
    private final AccountRepository accountRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final MerchantCategoryResolver categoryResolver;

    /**
     * 지정 기간의 확정 카드거래를 모두 조회하여 아직 분개가 없는 거래에 대해 전표를 생성한다.
     * 매일 배치(스케줄러)에서 최근 N일 범위로 호출하는 것을 권장.
     */
    @Transactional
    public int generateForPeriod(LocalDateTime from, LocalDateTime to) {
        int created = 0;
        for (CardTransactionProvider provider : providers) {
            String cardCompanyCode = provider.getCardCompanyCode();
            List<? extends CardTransaction> transactions = provider.findConfirmedTransactions(from, to);

            for (CardTransaction tx : transactions) {
                if (tx.isDeleted()) continue;

                boolean alreadyExists = journalEntryRepository
                        .findBySourceCardCompanyAndSourceRefIdAndSourceAndDeletedFalse(
                                cardCompanyCode, tx.getId(), JournalEntry.Source.CARD_IMPORT)
                        .isPresent();
                if (alreadyExists) continue;

                generateOne(cardCompanyCode, tx);
                created++;
            }
        }
        log.info("카드거래 1단계 분개 생성 완료: {}건", created);
        return created;
    }

    private void generateOne(String cardCompanyCode, CardTransaction tx) {
        // 할인 반영 실청구액 (요구사항: amount - benefit_amount = currency)
        BigDecimal currency = BigDecimal.valueOf(
                tx.getAmount() - (tx.getBenefitAmount() != null ? tx.getBenefitAmount() : 0L));

        Account liabilityAccount = accountRepository.findByCode(LIABILITY_ACCOUNT_CODE.get(cardCompanyCode))
                .orElseThrow(() -> new IllegalStateException("카드미지급금 계정 없음: " + cardCompanyCode));

        Optional<MerchantCategoryResolver.ResolvedResult> resolved = categoryResolver.resolve(tx.getMerchantName());

        JournalEntry entry = new JournalEntry();
        entry.setEntryDate(tx.getTransactionDate().toLocalDate());
        entry.setPaymentDate(tx.getPaymentDate() != null ? tx.getPaymentDate().toLocalDate() : null);
        entry.setVendor(tx.getMerchantName());
        entry.setSource(JournalEntry.Source.CARD_IMPORT);
        entry.setSourceCardCompany(cardCompanyCode);
        entry.setSourceRefId(tx.getId());
        entry.setConfirmed(true); // 카드결제 확정거래 기준이므로 즉시 확정

        if (resolved.isPresent() && resolved.get().ruleType == MerchantCategoryRule.RuleType.POINT_SETTLEMENT) {
            // 포인트로 카드대금 대납 — 비용 계정 미관여, 부채-자산 상계
            Account cardPointAccount = accountRepository.findByCode(CARD_POINT_ACCOUNT_CODE)
                    .orElseThrow(() -> new IllegalStateException("카드포인트 계정 없음"));
            entry.setDescription("카드포인트 결제");
            entry.addLine(liabilityAccount, currency, null, null);
            entry.addLine(cardPointAccount, null, currency, null);
        } else {
            // 일반 지출 — 비용 계정 결정 (미매칭 시 '미분류')
            Account expenseAccount;
            if (resolved.isPresent() && resolved.get().ruleType == MerchantCategoryRule.RuleType.ACCOUNT) {
                expenseAccount = resolved.get().account;
            } else {
                expenseAccount = accountRepository.findByCode(UNCLASSIFIED_ACCOUNT_CODE)
                        .orElseThrow(() -> new IllegalStateException("미분류 계정 없음"));
            }
            if (resolved.isPresent() && resolved.get().ruleType == MerchantCategoryRule.RuleType.MEMBER_TAG) {
                entry.setMemberTag(resolved.get().memberTag);
                // 가족구성원 태그만 있고 카테고리 규칙이 별도로 없는 경우 → 미분류로 두고 수동 확인 유도
                expenseAccount = accountRepository.findByCode(UNCLASSIFIED_ACCOUNT_CODE)
                        .orElseThrow(() -> new IllegalStateException("미분류 계정 없음"));
            }

            entry.setDescription(tx.getUsageType());
            entry.addLine(expenseAccount, currency, null, null);
            entry.addLine(liabilityAccount, null, currency, null);
        }

        journalEntryRepository.save(entry);
    }
}
