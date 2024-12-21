package com.trustme.dto.response;

import com.trustme.dto.TransferDto;
import org.springframework.http.HttpStatusCode;

public class TransferResponse extends Response<TransferDto> {
    public TransferResponse(HttpStatusCode code, String message, TransferDto result) {
        super(code, message, result);
    }
}
