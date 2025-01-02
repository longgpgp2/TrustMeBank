package com.trustme.dto.mapper;

import com.trustme.dto.UserDto;
import com.trustme.dto.request.UserEditRequest;
import com.trustme.dto.request.UserLoginRequest;
import com.trustme.dto.request.UserRegisterRequest;
import com.trustme.dto.response.LoginResponse;
import com.trustme.model.Role;
import com.trustme.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.Date;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "accountName", source = "user.accountName")
    @Mapping(target = "balance", source = "user.balance")
    @Mapping(target = "role", source = "user.role")
    @Mapping(target = "createdAt", source = "user.createdAt")
    @Mapping(target = "updatedAt", source = "user.updatedAt")
    @Mapping(target = "isDisabled", source = "user.isDisabled")
    UserDto toUserDto(User user);

}
