package com.test.zoom.service.journal;

import com.test.zoom.entity.journal.Account;
import com.test.zoom.entity.journal.CardSettlementAccount;
import com.test.zoom.entity.journal.JournalEntry;
import com.test.zoom.repository.journal.AccountRepository;
import com.test.zoom.repository.journal.CardSettlementAccountRepository;
import com.test.zoom.repository.journal.JournalEntryRepository;
import com.test.zoom.repository.journal.JournalLineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 2단계 분개: 카드사 결제일(payment_date) 도래 시, 그날 청구되는 카드미지급금 순증가액만큼
 * 예금 계좌에서 빠져나가는 것으로 간주하여 자동 생성. 실제 출금 여부는 아직 확인 안 됐으므로
 * is_confirmed=0으로 생성하고, 추후 검증 시 확정 처리한다 (옵션 C).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SettlementBatchService {

    private final CardSettlementAccountRepository cardSettlementAccountRepository;
    private final JournalLineRepository journalLineRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final AccountRepository accountRepository;

    /** 매일 실행 — 오늘이 결제일인 카드사에 대해 정산 분개를 생성한다 */
    @Transactional
    public int generateForDate(LocalDate today) {
        int created = 0;
        for (CardSettlementAccount mapping : cardSettlementAccountRepository.findAll()) {
            if (!mapping.isActive()) continue;

            String cardCompanyCode = mapping.getCardCompanyCode();
            Account liabilityAccount = resolveLiabilityAccount(cardCompanyCode);

            boolean alreadySettled = journalEntryRepository.existsBySourceAndSourceCardCompanyAndEntryDate(
                    JournalEntry.Source.SETTLEMENT, cardCompanyCode, today);
            if (alreadySettled) continue;

            BigDecimal netAmount = journalLineRepository.sumNetLiabilityByAccountAndPaymentDate(
                    liabilityAccount, today, JournalEntry.Source.CARD_IMPORT);

            if (netAmount == null || netAmount.compareTo(BigDecimal.ZERO) <= 0) continue;

            JournalEntry entry = new JournalEntry();
            entry.setEntryDate(today);
            entry.setPaymentDate(today);
            entry.setDescription(cardCompanyCode + " 카드대금 결제(청구확정)");
            entry.setSource(JournalEntry.Source.SETTLEMENT);
            entry.setSourceCardCompany(cardCompanyCode);
            entry.setConfirmed(false); // 실제 계좌 출금은 미확인 — 옵션 C

            entry.addLine(liabilityAccount, netAmount, null, null);
            entry.addLine(mapping.getAccount(), null, netAmount, null);

            journalEntryRepository.save(entry);
            created++;
        }
        log.info("카드결제 2단계(계좌출금) 분개 생성 완료: {}건", created);
        return created;
    }

    private Account resolveLiabilityAccount(String cardCompanyCode) {
        String code = switch (cardCompanyCode) {
            case "KOOKMIN" -> "2000";
            case "SAMSUNG" -> "2001";
            case "SHINHAN" -> "2002";
            case "WOORI" -> "2003";
            case "NONGHYUP" -> "2004";
            default -> throw new IllegalStateException("알 수 없는 카드사: " + cardCompanyCode);
        };
        return accountRepository.findByCode(code)
                .orElseThrow(() -> new IllegalStateException("카드미지급금 계정 없음: " + cardCompanyCode));
    }
}
