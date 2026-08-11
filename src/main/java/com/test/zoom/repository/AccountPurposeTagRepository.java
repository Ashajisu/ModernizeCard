package com.test.zoom.repository;

import com.test.zoom.entity.journal.AccountPurposeTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountPurposeTagRepository extends JpaRepository<AccountPurposeTag, Long> {

    List<AccountPurposeTag> findByAccountId(Long accountId);
    
    List<AccountPurposeTag> findByAccountIdIn(List<Long> accountIds);

    @Query("select a.id from AccountPurposeTag t join t.account a where t.fundSource = :fundSource and t.accountType = :accountType")
    List<Long> findAccountIdsByFundSourceAndAccountType(String fundSource, String accountType);
}
