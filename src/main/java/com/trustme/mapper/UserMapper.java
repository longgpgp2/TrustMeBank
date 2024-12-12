package com.trustme.mapper;

import com.trustme.dto.UserDto;
import com.trustme.dto.request.UserEditRequest;
import com.trustme.dto.request.UserLoginRequest;
import com.trustme.dto.request.UserRegisterRequest;
import com.trustme.dto.response.LoginResponse;
import com.trustme.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserLoginRequest request);
    User toUser(UserEditRequest request);
    LoginResponse toUserResponse(User user);
    UserDto toUserDto(User user);

//    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
