package com.test.zoom.repository.journal;

import com.test.zoom.dto.dashboard.DashboardItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DashboardItemRepository extends JpaRepository<DashboardItem, Long> {
    
    List<DashboardItem> findByVisibleTrueOrderBySortOrder();
    List<DashboardItem> findAllByOrderBySortOrder();
    Optional<DashboardItem> findByAccount_Code(String accountCode);
}