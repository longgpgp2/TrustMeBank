package com.trustme.dto.mapper;

import com.trustme.dto.LoanDto;
import com.trustme.dto.SavingDto;
import com.trustme.model.Loan;
import com.trustme.model.Saving;

public class CustomLoanMapper {
    public static LoanDto getLoanDto(Loan loan){
        return LoanDto.builder()
                .id(loan.getId())
                .borrowerAccountName(loan.getBorrower().getAccountName())
                .status(loan.getStatus())
                .startDate(loan.getStartDate())
                .endDate(loan.getEndDate())
                .interestRate(loan.getInterestRate())
                .amount(loan.getAmount())
                .createdAt(loan.getCreatedAt())
                .updatedAt(loan.getUpdatedAt())
                .build();
    }
}
