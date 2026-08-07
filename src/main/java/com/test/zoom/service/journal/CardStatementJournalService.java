package com.test.zoom.service.journal;

import com.test.zoom.dto.journal.response.CardStatementRegenerateResponse;
import com.test.zoom.entity.card.CardTransaction;
import com.test.zoom.entity.journal.Account;
import com.test.zoom.entity.journal.JournalEntry;
import com.test.zoom.provider.CardTransactionProvider;
import com.test.zoom.repository.journal.AccountRepository;
import com.test.zoom.repository.journal.JournalEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * 카드명세서 집계전표 생성.
 * 기준: usedCard(카드 식별값) + paymentDate(실제 카드대금 결제일, 사용자가 엑셀에서 직접 입력한 값) 정확히 일치.
 * 카드사별 결제주기를 계산하지 않는다 — paymentDate는 이미 확정된 값을 그대로 신뢰한다.
 * 모든 CardTransactionProvider를 순회해 usedCard로 카드사를 자동 판별한다 (카드사별 분기 없음).
 *
 * 분류 기준: CardTransaction.usageType == Account.name 인 계정을 조회, 그 계정의 실제 category로 분기.
 * (merchant_category_rule, usage_type_account_map, 문자열 매칭 모두 사용하지 않음)
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CardStatementJournalService {

    private static final Map<String, String> LIABILITY_ACCOUNT_CODE = Map.of(
            "KOOKMIN", "2000",
            "SAMSUNG", "2001",
            "SHINHAN", "2002",
            "WOORI", "2003",
            "NONGHYUP", "2004",
            "HYUNDAI", "2005"
    );
    private static final String UNCLASSIFIED_CODE = "9999";
    private static final String DEFAULT_USAGE_TYPE = "미지정";

    private final List<CardTransactionProvider> providers;
    private final AccountRepository accountRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final JournalAssembler journalAssembler;

    /**
     * @param cardCompanyCode 카드사 코드 (프론트에서 명시적으로 전달, 예: "SHINHAN") — 이 값으로 Provider를 특정한다
     * @param paymentDate     실제 카드대금 결제일 (사용자가 엑셀에서 직접 입력한 값, 예: 2026-08-12) — 정확히 일치하는 거래만 집계
     * @param confirmCascade  이 결제일에 이미 후속 SETTLEMENT 전표가 있을 때, 함께 삭제할지 여부.
     *                        false 상태에서 후속 전표가 발견되면 실제 처리 없이 안내 메시지만 던진다.
     */
    public CardStatementRegenerateResponse regenerate(
            String cardCompanyCode, LocalDate paymentDate, boolean confirmCascade) {

        // 1. cardCompanyCode로 Provider를 먼저 특정 (순회/추정 없음)
        CardTransactionProvider provider = providers.stream()
                .filter(p -> p.getCardCompanyCode().equals(cardCompanyCode))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 카드사 코드: " + cardCompanyCode));

        // 2. 특정된 Provider에게만 usedCard + paymentDate 조건 조회를 위임
        List<CardTransaction> transactions = provider
                .findConfirmedTransactionsByPaymentDate(paymentDate)
                .stream()
                .filter(tx -> !tx.isDeleted())
                .map(tx -> (CardTransaction) tx)
                .toList();

        if (transactions.isEmpty()) {
            throw new IllegalArgumentException(
                    cardCompanyCode + " 카드의 결제일 " + paymentDate + " 카드내역을 찾을 수 없습니다.");
        }

        // 3. 같은 결제일 기준 기존 CARD_IMPORT 전표 조회
        List<JournalEntry> existingCardImports = journalEntryRepository
                .findBySourceAndSourceCardCompanyAndPaymentDateAndDeletedFalse(
                        JournalEntry.Source.CARD_IMPORT, cardCompanyCode, paymentDate);

        // 4. 같은 결제일 기준 이미 생성된 후속 SETTLEMENT 전표 확인
        List<JournalEntry> relatedSettlements = journalEntryRepository
                .findBySourceAndSourceCardCompanyAndPaymentDateAndDeletedFalse(
                        JournalEntry.Source.SETTLEMENT, cardCompanyCode, paymentDate);

        if (!relatedSettlements.isEmpty() && !confirmCascade) {
            throw new IllegalStateException(
                    "이 카드명세서(결제일 " + paymentDate + ")를 기준으로 이미 계좌출금 정산 전표(" +
                            relatedSettlements.size() + "건)가 생성되어 있습니다. " +
                            "재생성하면 해당 정산 전표도 함께 삭제됩니다. 계속하려면 확인 후 다시 요청해주세요.");
        }

        // 5. 확인되었거나 관련 SETTLEMENT가 없으면 — 기존 CARD_IMPORT + 관련 SETTLEMENT 모두 소프트삭제
        existingCardImports.forEach(e -> e.setDeleted(true));
        relatedSettlements.forEach(e -> e.setDeleted(true));
        log.info("{} 결제일 {} 재생성: 기존 CARD_IMPORT {}건, 연계 SETTLEMENT {}건 소프트삭제",
                cardCompanyCode, paymentDate, existingCardImports.size(), relatedSettlements.size());

        // 6. usageType별 집계 — 금액은 currency 필드를 그대로 사용 (amount-benefitAmount 재계산 안 함)
        Map<String, BigDecimal> sumByUsageType = new LinkedHashMap<>();
        for (CardTransaction tx : transactions) {
            String usageType = (tx.getUsageType() != null && !tx.getUsageType().isBlank())
                    ? tx.getUsageType() : DEFAULT_USAGE_TYPE;
            BigDecimal amount = BigDecimal.valueOf(tx.getCurrency());
            sumByUsageType.merge(usageType, amount, BigDecimal::add);
        }

        Account liabilityAccount = accountRepository.findByCode(LIABILITY_ACCOUNT_CODE.get(cardCompanyCode))
                .orElseThrow(() -> new IllegalStateException("카드미지급금 계정 없음: " + cardCompanyCode));
        Account unclassified = accountRepository.findByCode(UNCLASSIFIED_CODE)
                .orElseThrow(() -> new IllegalStateException("미분류 계정 없음"));

        JournalEntry entry = journalAssembler.createEntryWithSource(
                paymentDate, cardCompanyCode + " " + paymentDate + " 카드명세서 집계",
                cardCompanyCode, JournalEntry.Source.CARD_IMPORT);
        entry.setSourceCardCompany(cardCompanyCode);
        entry.setPaymentDate(paymentDate);
        entry.setConfirmed(false);

        BigDecimal totalExpenseDebit = BigDecimal.ZERO;
        BigDecimal totalAssetCredit = BigDecimal.ZERO;
        BigDecimal totalRevenueCredit = BigDecimal.ZERO;

        for (Map.Entry<String, BigDecimal> e : sumByUsageType.entrySet()) {
            String usageType = e.getKey();
            BigDecimal amount = e.getValue();
            if (amount.compareTo(BigDecimal.ZERO) == 0) continue;

            // usageType == Account.name 으로 직접 조회 (category 무관 — 조회된 계정의 실제 category로 분기)
            Account mapped = resolveAccountByUsageType(usageType);

            if (mapped != null && mapped.getCategory() == Account.AccountCategory.EXPENSE) {
                journalAssembler.addLine(entry, mapped, amount, null, "카드명세서 집계(" + usageType + ")");
                totalExpenseDebit = totalExpenseDebit.add(amount);
            } else if (mapped != null && mapped.getCategory() == Account.AccountCategory.REVENUE) {
                // 수익 계정(급여/이자수익/앱테크/포인트 등) — 카드내역에는 보통 음수로 찍히므로 절대값으로 대변 처리
                BigDecimal absAmount = amount.abs();
                journalAssembler.addLine(entry, mapped, null, absAmount, "카드명세서 집계-수익(" + usageType + ")");
                totalRevenueCredit = totalRevenueCredit.add(absAmount);
            } else if (mapped != null && mapped.getCategory() == Account.AccountCategory.ASSET) {
                // 예: '카드포인트' — 비용이 아니라 자산 감소(대변)로 상계
                journalAssembler.addLine(entry, mapped, null, amount, "카드명세서 집계-자산상계(" + usageType + ")");
                totalAssetCredit = totalAssetCredit.add(amount);
            } else {
                // 일치하는 계정이 없거나 EXPENSE/ASSET/REVENUE 외 분류인 경우 — 미분류로 폴백
                journalAssembler.addLine(entry, unclassified, amount, null, "카드명세서 집계-미분류(" + usageType + ")");
                totalExpenseDebit = totalExpenseDebit.add(amount);
            }
        }

        BigDecimal liabilityCredit = totalExpenseDebit.subtract(totalAssetCredit).subtract(totalRevenueCredit);
        if (liabilityCredit.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("집계 결과 카드대금이 0 이하입니다. 데이터를 확인해주세요. (비용합계=" +
                    totalExpenseDebit + ", 자산상계=" + totalAssetCredit + ", 수익상계=" + totalRevenueCredit + ")");
        }
        journalAssembler.addLine(entry, liabilityAccount, null, liabilityCredit, "카드대금 집계");

        journalAssembler.finalizeBalance(entry);
        journalEntryRepository.save(entry);

        log.info("{} 결제일 {} 집계전표 생성 완료: 거래 {}건, 비용합계 {}, 자산상계 {}, 수익상계 {}, 카드대금 {}",
                cardCompanyCode, paymentDate, transactions.size(), totalExpenseDebit, totalAssetCredit, totalRevenueCredit, liabilityCredit);

        return new CardStatementRegenerateResponse(entry.getId(), cardCompanyCode, List.of(paymentDate));
    }
    
    /**
     * usageType == Account.name 으로 계정을 조회한다 (category 제한 없음).
     * 동일 이름이 여러 category에 존재할 가능성에 대비해 EXPENSE → ASSET → REVENUE 우선순위로 본다.
     */
    private Account resolveAccountByUsageType(String usageType) {
        List<Account> candidates = accountRepository.findByName(usageType);
        if (candidates.isEmpty()) return null;

        return candidates.stream()
                .filter(a -> a.getCategory() == Account.AccountCategory.EXPENSE)
                .findFirst()
                .or(() -> candidates.stream()
                        .filter(a -> a.getCategory() == Account.AccountCategory.ASSET)
                        .findFirst())
                .or(() -> candidates.stream()
                        .filter(a -> a.getCategory() == Account.AccountCategory.REVENUE)
                        .findFirst())
                .orElse(null);
    }
}