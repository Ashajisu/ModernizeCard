package com.test.zoom.repository.journal;

import com.test.zoom.entity.journal.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long>, JpaSpecificationExecutor<JournalEntry> {

    /**
     * 카드거래 중복 분개 생성 방지용 조회.
     * ⚠️ deleted=false 조건을 포함 — 삭제된 전표는 "없는 것"으로 취급해 배치가 재생성하도록 함.
     */
    Optional<JournalEntry> findBySourceCardCompanyAndSourceRefIdAndSourceAndDeletedFalse(
            String sourceCardCompany, Long sourceRefId, JournalEntry.Source source);

    /** 정기분개(적금/대출 등) 중복 생성 방지 — sourceRefId에 recurring_journal_rule.id 저장 */
    boolean existsBySourceAndSourceRefIdAndEntryDateBetweenAndDeletedFalse(
            JournalEntry.Source source, Long sourceRefId, LocalDate from, LocalDate to);

    List<JournalEntry> findByEntryDateBetweenAndDeletedFalse(LocalDate from, LocalDate to);

    boolean existsBySourceAndSourceCardCompanyAndEntryDateAndDeletedFalse(
            JournalEntry.Source source, String sourceCardCompany, LocalDate entryDate);
}
