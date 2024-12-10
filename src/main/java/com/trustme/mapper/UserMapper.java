package com.trustme.mapper;

import com.trustme.dto.request.UserRegisterRequest;
import com.trustme.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "createdAt", expression = "java(new java.util.Date())")
    @Mapping(target = "updatedAt", expression = "java(new java.util.Date())")
    User toUser (UserRegisterRequest userRegisterRequest);
}