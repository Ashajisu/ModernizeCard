package com.test.zoom.repository.journal;

import com.test.zoom.entity.journal.MerchantCategoryRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MerchantCategoryRuleRepository extends JpaRepository<MerchantCategoryRule, Long> {
    List<MerchantCategoryRule> findByActiveTrue();
}
