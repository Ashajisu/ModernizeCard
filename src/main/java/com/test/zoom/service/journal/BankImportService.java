package com.test.zoom.service.journal;

import com.test.zoom.dto.journal.BankImportRow;
import com.test.zoom.dto.journal.request.BankImportRequest;
import com.test.zoom.dto.journal.response.BankImportResult;
import com.test.zoom.entity.journal.Account;
import com.test.zoom.entity.journal.JournalEntry;
import com.test.zoom.repository.journal.AccountRepository;
import com.test.zoom.repository.journal.JournalEntryRepository;
import com.test.zoom.repository.journal.JournalLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 은행 거래내역 엑셀 업로드 → 미리보기(계정 추천 + 중복검출) → 저장(전표 생성).
 * 계정 추천은 MerchantCategoryResolver를 그대로 재사용 (적요를 가맹점명처럼 취급).
 * 저장 시 검증/분개 생성은 JournalAssembler를 사용해 수기입력과 동일한 규칙을 따른다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BankImportService {

    private static final DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String UNCLASSIFIED_CODE = "9999";

    private final AccountRepository accountRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final JournalLineRepository journalLineRepository;
    private final MerchantCategoryResolver categoryResolver;
    private final JournalAssembler journalAssembler;

    /** 계정 추천 + 중복검출만 수행, 아직 아무것도 저장하지 않는다 */
    @Transactional(readOnly = true)
    public List<BankImportRow> preview(BankImportRequest request) {
        Account targetAccount = findAccount(request.getTargetAccountId());
        Account unclassified = accountRepository.findByCode(UNCLASSIFIED_CODE).orElse(null);

        for (BankImportRow row : request.getRows()) {
            LocalDate date;
            try {
                date = parseDate(row.getTransactionDate());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(e.getMessage() + " (적요: '" + row.getDescription() + "')");
            }
            BigDecimal amount = resolveAmount(row);

            resolveCounterAccount(row, unclassified);

            boolean dup = amount.compareTo(BigDecimal.ZERO) > 0
                    && journalLineRepository.existsPossibleDuplicate(targetAccount.getId(), date, amount);
            row.setDuplicate(dup);
        }

        return request.getRows();
    }

    /**
     * 상대계정 결정 우선순위: 엑셀에 지정된 accountCode(유효한 경우) > MerchantCategoryResolver 추천 > 미분류.
     */
    private void resolveCounterAccount(BankImportRow row, Account unclassified) {
        if (row.getAccountCode() != null && !row.getAccountCode().isBlank()) {
            Account specified = accountRepository.findByCode(row.getAccountCode().trim()).orElse(null);
            if (specified != null) {
                row.setAccountId(specified.getId());
                row.setAccountName(specified.getName());
                return;
            }
            // 지정된 코드가 유효하지 않으면 리졸버로 폴백
        }

        var resolved = categoryResolver.resolve(row.getDescription());
        if (resolved.isPresent() && resolved.get().account != null) {
            row.setAccountId(resolved.get().account.getId());
            row.setAccountName(resolved.get().account.getName());
        } else if (unclassified != null) {
            row.setAccountId(unclassified.getId());
            row.setAccountName(unclassified.getName());
        }
    }

    /** 사용자가 확정한 상대계정으로 실제 전표(JournalEntry+JournalLine)를 생성 */
    public BankImportResult save(BankImportRequest request) {
        Account targetAccount = findAccount(request.getTargetAccountId());

        List<String> errors = new ArrayList<>();
        List<JournalEntry> toSave = new ArrayList<>();
        int skipped = 0;
        int rowNum = 1;

        for (BankImportRow row : request.getRows()) {
            rowNum++;
            if (row.isSkip()) {
                skipped++;
                continue;
            }

            BigDecimal amount = resolveAmount(row);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                errors.add("금액이 올바르지 않습니다. (엑셀 " + rowNum + "행: " + row.getDescription() + ")");
                continue;
            }
            if (row.getAccountId() == null) {
                errors.add("상대계정이 선택되지 않았습니다. (엑셀 " + rowNum + "행: " + row.getDescription() + ")");
                continue;
            }
            Account counterAccount = accountRepository.findById(row.getAccountId()).orElse(null);
            if (counterAccount == null) {
                errors.add("상대계정을 찾을 수 없습니다. id=" + row.getAccountId() + " (엑셀 " + rowNum + "행)");
                continue;
            }

            LocalDate date = parseDate(row.getTransactionDate());
            boolean isDeposit = row.getDeposit() != null && row.getDeposit().compareTo(BigDecimal.ZERO) > 0;

            JournalEntry entry = journalAssembler.createEntryWithSource(
                    date, row.getDescription(), row.getDescription(), JournalEntry.Source.BANK_IMPORT);

            if (isDeposit) {
                // 입금: 대상계좌(자산 증가, 차변) / 상대계정(대변)
                journalAssembler.addLine(entry, targetAccount, amount, null, "은행거래 자동입력");
                journalAssembler.addLine(entry, counterAccount, null, amount, "은행거래 자동입력");
            } else {
                // 출금: 상대계정(차변) / 대상계좌(자산 감소, 대변)
                journalAssembler.addLine(entry, counterAccount, amount, null, "은행거래 자동입력");
                journalAssembler.addLine(entry, targetAccount, null, amount, "은행거래 자동입력");
            }

            journalAssembler.finalizeBalance(entry);
            toSave.add(entry);
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("검증 실패:\n" + String.join("\n", errors));
        }

        journalEntryRepository.saveAll(toSave);
        return new BankImportResult(toSave.size(), skipped);
    }

    private Account findAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("대상 계좌를 찾을 수 없습니다. id=" + id));
    }

    private BigDecimal resolveAmount(BankImportRow row) {
        BigDecimal deposit = row.getDeposit() != null ? row.getDeposit() : BigDecimal.ZERO;
        BigDecimal withdrawal = row.getWithdrawal() != null ? row.getWithdrawal() : BigDecimal.ZERO;
        return deposit.compareTo(BigDecimal.ZERO) > 0 ? deposit : withdrawal;
    }

    private LocalDate parseDate(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("거래일자가 비어있는 행이 있습니다. (받은 값: " + raw + ")");
        }
        String trimmed = raw.trim();
        try {
            if (trimmed.matches("\\d{8}")) {
                return LocalDate.parse(trimmed, YYYYMMDD);
            }
            // 엑셀 날짜서식 셀이 시리얼 숫자로 넘어온 경우 (예: 48666) — 1899-12-30 기준 변환
            if (trimmed.matches("\\d{1,6}")) {
                long serial = Long.parseLong(trimmed);
                return LocalDate.of(1899, 12, 30).plusDays(serial);
            }
            return LocalDate.parse(trimmed); // yyyy-MM-dd
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "거래일자 형식이 올바르지 않습니다 (yyyy-MM-dd, yyyyMMdd, 또는 엑셀 시리얼 숫자만 허용): 받은 값 = '" + raw + "'");
        }
    }
}