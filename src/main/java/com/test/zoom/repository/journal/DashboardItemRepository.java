package com.test.zoom.repository.journal;

import com.test.zoom.dto.dashboard.DashboardItem;
import com.test.zoom.entity.journal.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DashboardItemRepository extends JpaRepository<DashboardItem, Long> {
    
    List<DashboardItem> findByVisibleTrueOrderBySortOrder();
    List<DashboardItem> findByChartVisibleTrueAndAccountCategory(Account.AccountCategory account_category);
    List<DashboardItem> findAllByOrderBySortOrder();
    Optional<DashboardItem> findByAccount_Code(String accountCode);
}