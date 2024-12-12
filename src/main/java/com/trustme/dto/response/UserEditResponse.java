package com.trustme.dto.response;

import org.springframework.http.HttpStatusCode;

public class UserEditResponse extends Response<String>{
    public UserEditResponse(HttpStatusCode code, String message, String result) {
        super(code, message, result);
    }
}
