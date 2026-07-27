package com.test.zoom.repository.journal;

import com.test.zoom.entity.journal.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByCode(String code);
}
