package com.trustme.dto.response;

import com.trustme.dto.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;
@Getter
@Setter
@NoArgsConstructor
public class UserEditResponse extends Response<UserDto>{
    public UserEditResponse(int code, String message, UserDto result) {
        super(code, message, result);
    }
}
