package com.trustme.dto.response;

import com.trustme.dto.ErrorDto;
import org.springframework.http.HttpStatusCode;

public class ErrorResponse extends Response<ErrorDto>{
    public ErrorResponse(HttpStatusCode code, String message, ErrorDto result) {
        super(code, message, result);
    }
}
