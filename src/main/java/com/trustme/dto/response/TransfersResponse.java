package com.trustme.dto.response;

import com.trustme.dto.TransferDto;
import org.springframework.http.HttpStatusCode;

import java.util.List;

public class TransfersResponse extends Response<List<TransferDto>> {
    public TransfersResponse(HttpStatusCode code, String message, List<TransferDto> result) {
        super(code, message, result);
    }
}
