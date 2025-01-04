package com.trustme.dto.response;

import com.trustme.dto.LoanDto;

public class LoanResponse extends Response<LoanDto> {

    public LoanResponse(int code, String message, LoanDto result) {
        super(code, message, result);
    }
}
