package com.trustme.repository;

import com.trustme.dto.response.LoansResponse;
import com.trustme.model.Loan;
import com.trustme.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findAllByBorrower(User borrower);

    Optional<Loan> findByIdAndBorrower(Long id, User borrower);
}
