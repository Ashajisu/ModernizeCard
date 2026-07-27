package com.test.zoom.entity.journal;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** 전표 헤더 (분개 묶음 단위) */
@Data
@Entity
@Table(name = "journal_entry")
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    /** 카드사 결제(청구)일 — 카드거래 분개에서만 채워짐, 2단계 정산 배치의 그룹핑 기준 */
    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "description")
    private String description;

    @Column(name = "vendor")
    private String vendor;

    /** 가족구성원 태깅 (예: 송승미) — 계정과목과 완전히 독립적인 별도 차원 */
    @Column(name = "member_tag")
    private String memberTag;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false)
    private Source source;

    @Column(name = "source_card_company")
    private String sourceCardCompany;

    /** 원본 카드 테이블 PK 또는 recurring_journal_rule ID — 중복 생성 방지 추적용 */
    @Column(name = "source_ref_id")
    private Long sourceRefId;

    @Column(name = "is_confirmed")
    @ColumnDefault("1")
    private boolean confirmed = true;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "created_at")
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "deleted")
    @ColumnDefault("0")
    private boolean deleted = false;

    @OneToMany(mappedBy = "journalEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JournalLine> lines = new ArrayList<>();

    public enum Source {
        MANUAL, CARD_IMPORT, SETTLEMENT, RECURRING
    }

    /** 분개 라인 추가 헬퍼 — 차변/대변 둘 중 하나에만 값이 들어감 */
    public JournalLine addLine(Account account, java.math.BigDecimal debit, java.math.BigDecimal credit, String memo) {
        JournalLine line = new JournalLine();
        line.setJournalEntry(this);
        line.setAccount(account);
        line.setDebitAmount(debit);
        line.setCreditAmount(credit);
        line.setMemo(memo);
        this.lines.add(line);
        return line;
    }

    /** 차변 합계 == 대변 합계 검증 */
    public boolean isBalanced() {
        java.math.BigDecimal debitSum = lines.stream().map(JournalLine::getDebitAmount)
                .filter(java.util.Objects::nonNull).reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        java.math.BigDecimal creditSum = lines.stream().map(JournalLine::getCreditAmount)
                .filter(java.util.Objects::nonNull).reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        return debitSum.compareTo(creditSum) == 0;
    }
}
