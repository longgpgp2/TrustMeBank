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
import org.springframework.http.ResponseEntity;
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
    private final UserService userService;
    private final AuthService authService;

    public UsersController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("/users")
    public ResponseEntity<UsersResponse> getUsers() {
        List<UserDto> users = userService.getUsers();
        UsersResponse usersResponse = new UsersResponse(
                StatusCode.OK.getHttpStatus(),
                StatusCode.OK.getStatusMessage(),
                users);
        return ResponseEntity.status(usersResponse.getCode()).body(usersResponse);
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponse> getUser(@RequestParam String accountName) {
        UserResponse userResponse = null;
        try {
            User user = userService.findUser(accountName);
             userResponse = new UserResponse(
                     StatusCode.OK.getHttpStatus(),
                     StatusCode.OK.getStatusMessage(),
                     CustomUserMapper.getUserDto(user));
        } catch (Exception e) {
            userResponse = new UserResponse(
                    ErrorCode.INVALID_USER.getHttpStatus(),
                    ErrorCode.INVALID_USER.getErrorMessage(),
                    null);
        }
        return ResponseEntity.status(userResponse.getCode()).body(userResponse);

    }
    @PostMapping("/user")
    public ResponseEntity<UserResponse> postUser(@RequestBody UserRegisterRequest userRegisterRequest) {
        UserResponse userResponse = authService.registerUser(userRegisterRequest);
        return ResponseEntity.status(userResponse.getCode()).body(userResponse);
    }

    @PatchMapping("/user")
    public ResponseEntity<UserEditResponse> putUser(@RequestBody UserEditRequest userEditRequest) {
        UserEditResponse userEditResponse = userService.editUser(userEditRequest);
        return ResponseEntity.status(userEditResponse.getCode()).body(userEditResponse);
    }

    @DeleteMapping("/user")
    public ResponseEntity<UserEditResponse> deleteUser(@RequestBody UserEditRequest userEditRequest) {
        UserEditResponse userEditResponse = userService.editUser(userEditRequest);
        return ResponseEntity.status(userEditResponse.getCode()).body(userEditResponse);
    }
}
