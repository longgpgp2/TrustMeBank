package com.trustme.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum LoanStatus {
    PENDING( "Loan request is being processed!"),
    ONGOING( "The borrower is currently on a debt!"),
    COMPLETED( "The loan has been fully repaid!"),
    DEFAULTED( "The borrower failed to repay the loan!");

    private String statusMessage;

}
