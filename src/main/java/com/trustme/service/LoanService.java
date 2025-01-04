package com.trustme.service;

import com.trustme.dto.LoanDto;
import com.trustme.dto.mapper.CustomLoanMapper;
import com.trustme.dto.request.LoanEditRequest;
import com.trustme.dto.request.LoanRequest;
import com.trustme.dto.response.LoanResponse;
import com.trustme.dto.response.LoansResponse;
import com.trustme.enums.LoanStatus;
import com.trustme.exception.exceptions.ResourceNotFoundException;
import com.trustme.model.Loan;
import com.trustme.model.User;
import com.trustme.repository.LoanRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for managing loan-related operations.
 */
@Service
@AllArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final AuthService authService;

    /**
     * Retrieves all loans for the currently authenticated user.
     *
     * @return a {@link LoansResponse} containing the list of loans.
     */
    public LoansResponse getAllLoansOfCurrentUser() {
        User user = authService.getCurrentUser();
        List<LoanDto> loanDtos = loanRepository
                .findAllByBorrower(user)
                .stream()
                .map(CustomLoanMapper::getLoanDto)
                .toList();
        return new LoansResponse(200, "Retrieved all loans from : " + user.getAccountName(), loanDtos);
    }

    /**
     * Retrieves all loans in the system.
     *
     * @return a {@link LoansResponse} containing the list of all loans.
     */
    public LoansResponse getAllLoans() {
        List<LoanDto> loanDtos = loanRepository
                .findAll()
                .stream()
                .map(CustomLoanMapper::getLoanDto)
                .toList();
        return new LoansResponse(200, "Retrieved all loans!", loanDtos);
    }

    /**
     * Retrieves details of a specific loan by its ID.
     *
     * @param id the ID of the loan to retrieve.
     * @return a {@link LoanResponse} containing the loan details.
     */
    public LoanResponse getOneLoan(Long id) {
        User user = authService.getCurrentUser();
        LoanDto loanDto = CustomLoanMapper.getLoanDto(loanRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Loan not found with ID: " + id)));
        return new LoanResponse(200, "Retrieve a loan by an admin: " + user.getAccountName(), loanDto);
    }

    /**
     * Retrieves details of a specific loan by its ID for the current user.
     *
     * @param id the ID of the loan to retrieve.
     * @return a {@link LoanResponse} containing the loan details.
     */
    public LoanResponse getOneLoanOfCurrentUser(Long id) {
        User user = authService.getCurrentUser();
        LoanDto loanDto = CustomLoanMapper.getLoanDto(loanRepository.findByIdAndBorrower(id, user).orElseThrow(
                () -> new ResourceNotFoundException("Loan not found with ID: " + id)));
        return new LoanResponse(200, "Retrieve a loan from user: " + user.getAccountName(), loanDto);
    }

    /**
     * Creates a new loan request for the currently authenticated user.
     *
     * @param loanRequest the details of the loan to create.
     * @return a {@link LoanResponse} indicating the loan creation status.
     */
    public LoanResponse requestLoan(LoanRequest loanRequest) {

        Loan loan = Loan.builder()
                .borrower(authService.getCurrentUser())
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusMonths(loanRequest.getDuration()))
                .createdAt(LocalDateTime.now())
                .status(LoanStatus.PENDING)
                .interestRate(loanRequest.getInterestRate())
                .amount(loanRequest.getAmount())
                .build();
        loanRepository.save(loan);

        return new LoanResponse(201, "Created", null);
    }

    /**
     * Change the status of a loan.
     *
     * @param loanEditRequest the details of the loan to change.
     * @param id
     * @return a {@link LoanResponse} indicating the loan update status.
     */
    public LoanResponse editLoan(LoanEditRequest loanEditRequest, Long id) {
        Loan loan = loanRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Loan not found with id: " + id));
        if(loanEditRequest.getStatus()!=null) {
            loan.setEndDate(LocalDateTime.now().plusMonths(loanEditRequest.getDuration()));
        }
        loan.setStatus(loanEditRequest.getStatus());
        LoanDto loanDto = CustomLoanMapper.getLoanDto(loanRepository.save(loan));
        return new LoanResponse(204, "Updated!", loanDto);
    }
}
