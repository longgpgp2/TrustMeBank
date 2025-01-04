package com.trustme.controller;

import java.util.List;

import com.trustme.dto.UserDto;
import com.trustme.dto.request.UserRegisterRequest;
import com.trustme.enums.StatusCode;
import com.trustme.dto.mapper.CustomUserMapper;
import com.trustme.service.AuthService;
import com.trustme.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.trustme.dto.request.UserEditRequest;
import com.trustme.dto.response.UserEditResponse;
import com.trustme.dto.response.UserResponse;
import com.trustme.dto.response.UsersResponse;
import com.trustme.model.User;

@AllArgsConstructor
@RestController
@RequestMapping("/admin/users")
public class UsersController {
    private final UserService userService;
    private final AuthService authService;


    @GetMapping()
    public ResponseEntity<UsersResponse> getUsers() {
        List<UserDto> users = userService.getUsers();
        UsersResponse usersResponse = new UsersResponse(
                StatusCode.OK.getHttpStatus(),
                StatusCode.OK.getStatusMessage(),
                users);
        return ResponseEntity.status(usersResponse.getCode()).body(usersResponse);
    }

    @GetMapping("/{accountName}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String accountName) {
        User user = userService.findUser(accountName);
        UserResponse userResponse = new UserResponse(
                 StatusCode.OK.getHttpStatus(),
                 StatusCode.OK.getStatusMessage(),
                 CustomUserMapper.getUserDto(user));
        return ResponseEntity.status(userResponse.getCode()).body(userResponse);

    }
    @PostMapping()
    public ResponseEntity<UserResponse> postUser(@RequestBody UserRegisterRequest userRegisterRequest) {
        UserResponse userResponse = authService.registerUser(userRegisterRequest);
        return ResponseEntity.status(userResponse.getCode()).body(userResponse);
    }

    @PatchMapping()
    public ResponseEntity<UserEditResponse> putUser(@RequestBody UserEditRequest userEditRequest) {
        UserEditResponse userEditResponse = userService.editUser(userEditRequest);
        return ResponseEntity.status(userEditResponse.getCode()).body(userEditResponse);
    }

    @DeleteMapping()
    public ResponseEntity<UserEditResponse> deleteUser(@RequestBody UserEditRequest userEditRequest) {
        UserEditResponse userEditResponse = userService.editUser(userEditRequest);
        return ResponseEntity.status(userEditResponse.getCode()).body(userEditResponse);
    }
}
