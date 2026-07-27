package com.test.zoom.repository.journal;

import com.test.zoom.entity.journal.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
