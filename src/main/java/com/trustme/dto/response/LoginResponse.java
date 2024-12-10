package com.trustme.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
@Builder
public class LoginResponse extends Response<String> {
    public LoginResponse(HttpStatusCode code, String message, String token) {
        super(code, message, token);
    }
    public LoginResponse() {
        super(null, null, null);
    }
}
