package com.trustme.dto.response;

import com.trustme.dto.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UsersResponse extends Response<List<UserDto>>{
    public UsersResponse(int code, String message, List<UserDto> result) {
        super(code, message, result);
    }

}
