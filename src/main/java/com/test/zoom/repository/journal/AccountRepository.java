package com.test.zoom.repository.journal;

import com.test.zoom.entity.journal.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByCode(String code);

    List<Account> findByParent_Id(Long parentId);

    List<Account> findByActiveTrue();

    List<Account> findByName(String name);

    /** usageType == Account.name 매핑 조회용 (usage_type_account_map 대체) */
    Optional<Account> findByCategoryAndName(Account.AccountCategory category, String name);
}

