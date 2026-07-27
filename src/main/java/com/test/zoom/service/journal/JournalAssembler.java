package com.test.zoom.service.journal;

import com.test.zoom.dto.journal.request.JournalCreateRequest;
import com.test.zoom.dto.journal.request.JournalLineRequest;
import com.test.zoom.entity.card.CardTransaction;
import com.test.zoom.entity.journal.Account;
import com.test.zoom.entity.journal.JournalEntry;
import com.test.zoom.entity.journal.Loan;
import com.test.zoom.entity.journal.RecurringJournalRule;
import com.test.zoom.repository.journal.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class JournalAssembler {

    private final AccountRepository accountRepository;

    /**
     * 수기입력 전표 생성
     */
    public JournalEntry from(JournalCreateRequest request) {

        JournalEntry entry = createEntry(
                request.getEntryDate(),
                request.getDescription(),
                request.getVendor(),
                request.getMemberTag(),
                JournalEntry.Source.MANUAL
        );

        for (JournalLineRequest line : request.getLines()) {

            addLine(
                    entry,
                    line.getAccountId(),
                    line.getDebitAmount(),
                    line.getCreditAmount(),
                    line.getMemo()
            );
        }

        validateBalance(entry);

        return entry;
    }

    
//    public JournalEntry from(CardTransaction transaction){
//    }
//
//    public JournalEntry from(RecurringJournalRule rule){
//    }
//
//    public JournalEntry from(Loan loan){
//    }

    /**
     * 공통 헤더 생성
     */
    private JournalEntry createEntry(
            java.time.LocalDate entryDate,
            String description,
            String vendor,
            String memberTag,
            JournalEntry.Source source
    ) {

        JournalEntry entry = new JournalEntry();

        entry.setEntryDate(entryDate);
        entry.setDescription(description);
        entry.setVendor(vendor);
        entry.setMemberTag(memberTag);
        entry.setSource(source);

        return entry;
    }

    /**
     * 공통 분개라인 추가
     */
    protected void addLine(
            JournalEntry entry,
            Long accountId,
            BigDecimal debit,
            BigDecimal credit,
            String memo
    ) {

        Account account = findAccount(accountId);

        debit = amount(debit);
        credit = amount(credit);

        validateLine(debit, credit);

        entry.addLine(
                account,
                debit,
                credit,
                memo
        );
    }
    
    /**
     * 공통 분개라인 추가 - 오버로딩: 미리 조회하거나 캐싱한 Account 객체를 넘겨 Account 버전 사용
     * 
     * **/
    protected void addLine(
            JournalEntry entry,
            Account account,
            BigDecimal debit,
            BigDecimal credit,
            String memo
    ){
        debit = amount(debit);
        credit = amount(credit);

        validateLine(debit, credit);

        entry.addLine(
                account,
                debit,
                credit,
                memo
        );
    }

    /**
     * 계정 조회
     */
    private Account findAccount(Long accountId) {

        return accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "계정과목이 존재하지 않습니다. id=" + accountId));
    }

    protected BigDecimal amount(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }


    /**
     * null -> ZERO
     */
    private BigDecimal zeroIfNull(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    /**
     * 분개라인 검증
     */
    private void validateLine(
            BigDecimal debit,
            BigDecimal credit
    ) {

        if (debit.signum() < 0) {
            throw new IllegalArgumentException("차변금액은 음수일 수 없습니다.");
        }

        if (credit.signum() < 0) {
            throw new IllegalArgumentException("대변금액은 음수일 수 없습니다.");
        }

        if (debit.signum() == 0 && credit.signum() == 0) {
            throw new IllegalArgumentException("차변 또는 대변 금액을 입력해야 합니다.");
        }

        if (debit.signum() > 0 && credit.signum() > 0) {
            throw new IllegalArgumentException("차변과 대변을 동시에 입력할 수 없습니다.");
        }
    }

    /**
     * 차변 = 대변 검증
     */
    private void validateBalance(JournalEntry entry) {

        if (!entry.isBalanced()) {
            throw new IllegalArgumentException("차변합계와 대변합계가 일치하지 않습니다.");
        }
    }
    
    /**
     * 전표 헤더 필드 갱신 (수정 시 사용)
     */
    public void updateHeader(
            JournalEntry entry,
            java.time.LocalDate entryDate,
            String description,
            String vendor,
            String memberTag
    ) {
        entry.setEntryDate(entryDate);
        entry.setDescription(description);
        entry.setVendor(vendor);
        entry.setMemberTag(memberTag);
    }

    /**
     * 기존 분개라인을 전부 제거하고 새 라인으로 교체 (수정 시 사용).
     * JournalEntry.lines가 orphanRemoval=true이므로 clear() 시 기존 라인은 DB에서 삭제된다.
     */
    public void replaceLines(JournalEntry entry, java.util.List<JournalLineRequest> newLines) {
        entry.getLines().clear();

        for (JournalLineRequest line : newLines) {
            addLine(
                    entry,
                    line.getAccountId(),
                    line.getDebitAmount(),
                    line.getCreditAmount(),
                    line.getMemo()
            );
        }

        validateBalance(entry);
    }


}