package com.trustme.controller;

import java.util.List;
import java.util.Optional;

import com.trustme.dto.UserDto;
import com.trustme.dto.request.UserRegisterRequest;
import com.trustme.enums.ErrorCode;
import com.trustme.enums.StatusCode;
import com.trustme.mapper.CustomUserMapper;
import com.trustme.service.AuthService;
import com.trustme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.trustme.dto.request.UserEditRequest;
import com.trustme.dto.response.UserEditResponse;
import com.trustme.dto.response.UserResponse;
import com.trustme.dto.response.UsersResponse;
import com.trustme.model.User;
import com.trustme.repository.UserRepository;

@RestController
@RequestMapping("/admin")
public class UsersController {
    @Autowired
    UserService userService;
    @Autowired
    AuthService authService;
    @GetMapping("/users")
    public UsersResponse getUsers() {
        List<UserDto> users = userService.getUsers();
        return new UsersResponse(StatusCode.OK.getHttpStatus(), StatusCode.OK.getStatusMessage(), users);
    }

    @GetMapping("/user")
    public UserResponse getUser(@RequestParam String accountName) {
        try {
            User user = userService.findUser(accountName);
            return new UserResponse(StatusCode.OK.getHttpStatus(), StatusCode.OK.getStatusMessage(), CustomUserMapper.getUserDto(user));
        } catch (Exception e) {
            return  new UserResponse(ErrorCode.INVALID_USER.getHttpStatus(), ErrorCode.INVALID_USER.getErrorMessage(), null);
        }

    }
    @PostMapping("/user")
    public UserResponse postUser(@RequestBody UserRegisterRequest userRegisterRequest) {
        return authService.registerUser(userRegisterRequest);
    }

    @PatchMapping("/user")
    public UserEditResponse putUser(@RequestBody UserEditRequest userEditRequest) {
        return userService.editUser(userEditRequest);
    }

    @DeleteMapping("/user")
    public UserEditResponse deleteUser(@RequestBody UserEditRequest userEditRequest) {
        return userService.editUser(userEditRequest);
    }
}
