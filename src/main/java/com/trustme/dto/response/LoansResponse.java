package com.trustme.dto.response;

import com.trustme.dto.LoanDto;

import java.util.List;

public class LoansResponse extends Response<List<LoanDto>> {
    public LoansResponse(int code, String message, List<LoanDto> result) {
        super(code, message, result);
    }
}
