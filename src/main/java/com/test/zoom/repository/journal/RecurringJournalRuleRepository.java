package com.test.zoom.repository.journal;

import com.test.zoom.entity.journal.RecurringJournalRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecurringJournalRuleRepository extends JpaRepository<RecurringJournalRule, Long> {
    List<RecurringJournalRule> findByActiveTrueAndFrequencyAndDayOfMonth(
            RecurringJournalRule.Frequency frequency, Integer dayOfMonth);

    List<RecurringJournalRule> findByActiveTrueAndFrequencyAndMonthOfYearAndDayOfMonth(
            RecurringJournalRule.Frequency frequency, Integer monthOfYear, Integer dayOfMonth);

    /** 현금흐름 계산용 — 적금이체/대출상환 등 규칙 유형별 조회 (활성 여부 무관, 과거 데이터 포함) */
    List<RecurringJournalRule> findByRuleType(RecurringJournalRule.RuleType ruleType);
}
