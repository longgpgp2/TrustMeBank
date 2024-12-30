package com.trustme.dto.response;

import lombok.*;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponse extends Response<String> {
    public LoginResponse(int code, String message, String token) {
        super(code, message, token);
    }
}
