package com.trustme.dto.response;

import com.trustme.model.User;
import org.springframework.http.HttpStatusCode;

public class UserResponse extends Response<User>{
    public UserResponse(HttpStatusCode code, String message, User result) {
        super(code, message, result);
    }
}
