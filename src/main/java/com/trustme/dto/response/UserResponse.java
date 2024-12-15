package com.trustme.dto.response;

import com.trustme.dto.UserDto;
import com.trustme.model.User;
import org.springframework.http.HttpStatusCode;

public class UserResponse extends Response<UserDto>{
    public UserResponse(HttpStatusCode code, String message, UserDto result) {
        super(code, message, result);
    }
}
