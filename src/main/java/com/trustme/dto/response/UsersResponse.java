package com.trustme.dto.response;

import com.trustme.dto.UserDto;
import com.trustme.mapper.UserMapper;
import com.trustme.model.User;
import org.springframework.http.HttpStatusCode;

import java.util.ArrayList;
import java.util.List;

public class UsersResponse extends Response<List<UserDto>>{
    private UserMapper userMapper;
    public UsersResponse(HttpStatusCode code, String message, List<User> result) {
        super(code, message, null);
        List<UserDto> usersDto = new ArrayList<>();
        result.stream().forEach(user -> usersDto.add(userMapper.toUserDto(user)));
    }

}
