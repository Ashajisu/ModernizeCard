package com.test.zoom.repository.journal;

import com.test.zoom.entity.journal.AccountPurposeTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountPurposeTagRepository extends JpaRepository<AccountPurposeTag, Long> {

    List<AccountPurposeTag> findByAccountId(Long accountId);
    
    List<AccountPurposeTag> findByAccountIdIn(List<Long> accountIds);

    @Query("select a.id from AccountPurposeTag t join t.account a where t.fundSource = :fundSource and t.accountType = :accountType")
    List<Long> findAccountIdsByFundSourceAndAccountType(String fundSource, String accountType);
    
    /** 미지정 흐름 계산용 — 태그가 아예 없는 계정을 가려내기 위해 태그된 전체 account_id 조회 */
    @Query("SELECT DISTINCT t.account.id FROM AccountPurposeTag t")
    List<Long> findAllTaggedAccountIds();
}
