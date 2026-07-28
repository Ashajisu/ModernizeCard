package com.test.zoom.service.journal;

import com.test.zoom.dto.journal.OpeningBalanceRow;
import com.test.zoom.entity.journal.Account;
import com.test.zoom.entity.journal.JournalEntry;
import com.test.zoom.entity.journal.JournalLine;
import com.test.zoom.repository.journal.AccountRepository;
import com.test.zoom.repository.journal.JournalEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 초기 잔액(개시잔액) 등록.
 * onExcelSave 계약에 맞춰 List<OpeningBalanceRow>를 받아 처리한 뒤,
 * 같은 리스트에 journalEntryId를 채워 그대로 반환한다 (프론트 tableList 병합용).
 */
@Service
@RequiredArgsConstructor
@Transactional
public class OpeningBalanceService {

    private static final DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final AccountRepository accountRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final JournalAssembler journalAssembler;

    public List<OpeningBalanceRow> findAll() {
        List<JournalEntry> journalEntryList = journalEntryRepository.findBySourceAndDeletedFalse(JournalEntry.Source.OPENING);
        List<OpeningBalanceRow> result = new ArrayList<>();

        for (JournalEntry entry : journalEntryList) {
            for (JournalLine line : entry.getLines()) {
                // 개시잔액은 차변 또는 대변 한쪽만 존재
                BigDecimal amount = line.getDebitAmount().compareTo(BigDecimal.ZERO) > 0
                        ? line.getDebitAmount()
                        : line.getCreditAmount();
                OpeningBalanceRow row = new OpeningBalanceRow();

                row.setAccountCode(line.getAccount().getCode());
                row.setAmount(amount);
                row.setAccountName(line.getAccount().getName());
                row.setAsOfDate(entry.getEntryDate().toString());
                row.setJournalEntryId(entry.getId());

                result.add(row);
            }
        }
        return result;
    }
    
    public List<OpeningBalanceRow> importRows(List<OpeningBalanceRow> rows) {
        if (rows == null || rows.isEmpty()) {
            throw new IllegalArgumentException("업로드할 데이터가 없습니다.");
        }

        LocalDate asOfDate = resolveAsOfDate(rows);

        List<String> errors = new ArrayList<>();
        List<LineData> lines = new ArrayList<>();

        int rowNum = 1;
        for (OpeningBalanceRow row : rows) {
            rowNum++;
            String code = row.getAccountCode();
            BigDecimal amount = row.getAmount();

            if (code == null || code.isBlank()) continue;
            if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) continue;

            Account account = accountRepository.findByCode(code.trim()).orElse(null);
            if (account == null) {
                errors.add("계정코드 '" + code + "' 를 찾을 수 없습니다. (엑셀 " + rowNum + "행)");
                continue;
            }
            if (account.getCategory() != Account.AccountCategory.ASSET
                    && account.getCategory() != Account.AccountCategory.LIABILITY) {
                errors.add("계정 '" + account.getName() + "'(" + code + ")는 자산/부채 계정이 아니라 개시잔액 대상이 아닙니다.");
                continue;
            }

            lines.add(new LineData(account, amount));
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("검증 실패:\n" + String.join("\n", errors));
        }
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("등록할 개시잔액 데이터가 없습니다. 금액이 채워진 행이 있는지 확인해주세요.");
        }

        // 기존 개시잔액 전표는 소프트 삭제 후 새로 생성 (재업로드 시 대체)
        journalEntryRepository.findBySourceAndDeletedFalse(JournalEntry.Source.OPENING)
                .forEach(e -> e.setDeleted(true));

        JournalEntry entry = journalAssembler.createOpeningBalanceEntry(asOfDate, "초기 잔액 등록 (엑셀 업로드)");
        entry.setConfirmed(true);
        entry.setConfirmedAt(LocalDateTime.now());

        BigDecimal totalAssetDebit = BigDecimal.ZERO;
        BigDecimal totalLiabilityCredit = BigDecimal.ZERO;

        for (LineData line : lines) {
            if (line.account.getCategory() == Account.AccountCategory.ASSET) {
                journalAssembler.addLine(entry, line.account, line.amount, null, "개시잔액");
                totalAssetDebit = totalAssetDebit.add(line.amount);
            } else {
                journalAssembler.addLine(entry, line.account, null, line.amount, "개시잔액");
                totalLiabilityCredit = totalLiabilityCredit.add(line.amount);
            }
        }

        BigDecimal netDiff = totalAssetDebit.subtract(totalLiabilityCredit);
        if (netDiff.compareTo(BigDecimal.ZERO) != 0) {
            Account equity = accountRepository.findByCode("9000")
                    .orElseThrow(() -> new IllegalStateException("개시잔액(자본) 계정이 없습니다. schema_alter_v4.sql을 실행했는지 확인해주세요."));
            if (netDiff.compareTo(BigDecimal.ZERO) > 0) {
                journalAssembler.addLine(entry, equity, null, netDiff, "개시잔액 상계(자본)");
            } else {
                journalAssembler.addLine(entry, equity, netDiff.abs(), null, "개시잔액 상계(자본)");
            }
        }

        journalAssembler.finalizeBalance(entry);
        journalEntryRepository.save(entry);

        // onExcelSave 계약: 받은 배열 그대로에 결과(journalEntryId)만 채워 반환
        rows.forEach(r -> r.setJournalEntryId(entry.getId()));
        return rows;
    }

    /** 모든 행에 반복 입력된 asOfDate 중 첫 값 사용, 서로 다른 값이 섞여 있으면 오류 */
    private LocalDate resolveAsOfDate(List<OpeningBalanceRow> rows) {
        String first = rows.stream()
                .map(OpeningBalanceRow::getAsOfDate)
                .filter(d -> d != null && !d.isBlank())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("기준일자(asOfDate)가 입력된 행이 없습니다."));

        boolean allSame = rows.stream()
                .map(OpeningBalanceRow::getAsOfDate)
                .filter(d -> d != null && !d.isBlank())
                .allMatch(first::equals);
        if (!allSame) {
            throw new IllegalArgumentException("모든 행의 기준일자(asOfDate)가 동일해야 합니다.");
        }

        return parseDate(first);
    }

    private LocalDate parseDate(String raw) {
        String trimmed = raw.trim();
        try {
            if (trimmed.matches("\\d{8}")) {
                return LocalDate.parse(trimmed, YYYYMMDD);
            }
            return LocalDate.parse(trimmed); // yyyy-MM-dd
        } catch (Exception e) {
            throw new IllegalArgumentException("기준일자 형식이 올바르지 않습니다 (yyyy-MM-dd 또는 yyyyMMdd): " + raw);
        }
    }

    private record LineData(Account account, BigDecimal amount) {}
}