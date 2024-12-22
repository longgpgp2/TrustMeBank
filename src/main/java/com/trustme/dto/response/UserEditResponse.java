package com.trustme.dto.response;

import com.trustme.dto.UserDto;
import org.springframework.http.HttpStatusCode;

public class UserEditResponse extends Response<UserDto>{
    public UserEditResponse(HttpStatusCode code, String message, UserDto result) {
        super(code, message, result);
    }
}
