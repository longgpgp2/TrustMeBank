package com.trustme.dto.response;

import com.trustme.dto.UserDto;
import com.trustme.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;
@Getter
@Setter
@NoArgsConstructor
public class UserResponse extends Response<UserDto>{
    public UserResponse(int code, String message, UserDto result) {
        super(code, message, result);
    }
}
