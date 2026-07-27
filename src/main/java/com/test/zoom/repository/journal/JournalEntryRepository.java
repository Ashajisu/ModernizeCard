package com.test.zoom.repository.journal;

import com.test.zoom.entity.journal.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {

    /** 카드거래 중복 분개 생성 방지용 조회 */
    Optional<JournalEntry> findBySourceCardCompanyAndSourceRefIdAndSource(
            String sourceCardCompany, Long sourceRefId, JournalEntry.Source source);

    /** 정기분개(적금/대출 등) 중복 생성 방지 — sourceRefId에 recurring_journal_rule.id 저장 */
    boolean existsBySourceAndSourceRefIdAndEntryDateBetween(
            JournalEntry.Source source, Long sourceRefId, LocalDate from, LocalDate to);

    List<JournalEntry> findByEntryDateBetweenAndDeletedFalse(LocalDate from, LocalDate to);

    boolean existsBySourceAndSourceCardCompanyAndEntryDate(
            JournalEntry.Source source, String sourceCardCompany, LocalDate entryDate);
}
