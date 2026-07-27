package com.test.zoom.repository.journal;

import com.test.zoom.entity.journal.CardSettlementAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardSettlementAccountRepository extends JpaRepository<CardSettlementAccount, Long> {
    Optional<CardSettlementAccount> findByCardCompanyCodeAndActiveTrue(String cardCompanyCode);
}
