package com.test.zoom.repository.journal;

import com.test.zoom.entity.journal.RecurringJournalRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecurringJournalRuleRepository extends JpaRepository<RecurringJournalRule, Long> {
    List<RecurringJournalRule> findByActiveTrueAndFrequencyAndDayOfMonth(
            RecurringJournalRule.Frequency frequency, Integer dayOfMonth);

    List<RecurringJournalRule> findByActiveTrueAndFrequencyAndMonthOfYearAndDayOfMonth(
            RecurringJournalRule.Frequency frequency, Integer monthOfYear, Integer dayOfMonth);
}
