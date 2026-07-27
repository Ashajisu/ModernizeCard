package com.test.zoom.service.journal;

import com.test.zoom.entity.journal.JournalEntry;
import com.test.zoom.entity.journal.JournalLine;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * JournalSearchRequest → Specification 변환 헬퍼.
 * ⚠️ 금액(minAmount/maxAmount) 조건은 라인의 debitAmount 기준으로만 필터링한다.
 *    다중라인(3줄 이상) 전표에서 대변 전용 금액 검색은 지원하지 않음 — 필요 시 확장 고려.
 *    월별/계정별/금액별/날짜별/거래처별 동적 검색을 지원
 */
public class JournalEntrySpecifications {

    private JournalEntrySpecifications() {}

    public static Specification<JournalEntry> notDeleted() {
        return (root, query, cb) -> cb.isFalse(root.get("deleted"));
    }

    public static Specification<JournalEntry> entryDateBetween(LocalDate from, LocalDate to) {
        return (root, query, cb) -> {
            if (from == null && to == null) return cb.conjunction();
            if (from != null && to != null) return cb.between(root.get("entryDate"), from, to);
            if (from != null) return cb.greaterThanOrEqualTo(root.get("entryDate"), from);
            return cb.lessThanOrEqualTo(root.get("entryDate"), to);
        };
    }

    public static Specification<JournalEntry> vendorLike(String vendor) {
        return (root, query, cb) -> {
            if (vendor == null || vendor.isBlank()) return cb.conjunction();
            return cb.like(root.get("vendor"), "%" + vendor + "%");
        };
    }

    public static Specification<JournalEntry> memberTagEquals(String memberTag) {
        return (root, query, cb) -> {
            if (memberTag == null || memberTag.isBlank()) return cb.conjunction();
            return cb.equal(root.get("memberTag"), memberTag);
        };
    }

    public static Specification<JournalEntry> sourceEquals(JournalEntry.Source source) {
        return (root, query, cb) -> {
            if (source == null) return cb.conjunction();
            return cb.equal(root.get("source"), source);
        };
    }

    public static Specification<JournalEntry> confirmedEquals(Boolean confirmed) {
        return (root, query, cb) -> {
            if (confirmed == null) return cb.conjunction();
            return cb.equal(root.get("confirmed"), confirmed);
        };
    }

    public static Specification<JournalEntry> accountIdEquals(Long accountId) {
        return (root, query, cb) -> {
            if (accountId == null) return cb.conjunction();
            Join<JournalEntry, JournalLine> lines = root.join("lines");
            query.distinct(true);
            return cb.equal(lines.get("account").get("id"), accountId);
        };
    }

    public static Specification<JournalEntry> amountBetween(BigDecimal min, BigDecimal max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return cb.conjunction();
            Join<JournalEntry, JournalLine> lines = root.join("lines");
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();
            if (min != null) predicates.add(cb.greaterThanOrEqualTo(lines.get("debitAmount"), min));
            if (max != null) predicates.add(cb.lessThanOrEqualTo(lines.get("debitAmount"), max));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
