package com.trustme.dto.mapper;

import com.trustme.dto.UserDto;
import com.trustme.model.User;

public class CustomUserMapper {
    /**
     * Map an instance of User to UserDto
     * @return UserDto
     */
    public static UserDto getUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getAccountName(),
                user.getBalance(),
                user.getRole().getRoles(),
                user.getEmail(),
                user.getPhone(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.isDisabled()
        );
    }
}
