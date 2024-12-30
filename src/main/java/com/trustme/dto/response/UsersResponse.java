package com.trustme.dto.response;

import com.trustme.dto.UserDto;
import com.trustme.mapper.UserMapper;
import com.trustme.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UsersResponse extends Response<List<UserDto>>{
    public UsersResponse(int code, String message, List<UserDto> result) {
        super(code, message, result);
    }

}
