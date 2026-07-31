package com.test.zoom.repository;

import com.test.zoom.entity.journal.AccountPurposeTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountPurposeTagRepository extends JpaRepository<AccountPurposeTag, Long> {

    List<AccountPurposeTag> findByAccountId(Long accountId);
    
    List<AccountPurposeTag> findByAccountIdIn(List<Long> accountIds);
}
