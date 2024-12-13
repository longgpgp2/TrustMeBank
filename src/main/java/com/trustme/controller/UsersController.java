package com.trustme.controller;

import java.util.List;
import java.util.Optional;

import com.trustme.dto.UserDto;
import com.trustme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @GetMapping("/users")
    public UsersResponse getUsers() {
        List<UserDto> users = userService.getUsers();
        return new UsersResponse(HttpStatus.OK, "OK", users);
    }

    @GetMapping("/user")
    public UserResponse getUser(@RequestParam String accountName) {
        try {
            User user = userService.findUser(accountName);
            return new UserResponse(HttpStatus.OK, "OK", userService.getUserDto(user));
        } catch (Exception e) {
            return  new UserResponse(HttpStatus.NOT_ACCEPTABLE, "Invalid user", null);
        }

    }

    @PostMapping("/user")
    public UserEditResponse postUser(@RequestBody UserEditRequest userEditRequest) {
        Optional<User> optionalUser = userRepository.findByAccountName(userEditRequest.getTargetAccount());
        if (optionalUser.isEmpty())
            return new UserEditResponse(HttpStatus.NOT_ACCEPTABLE, "Invalid user", null);
        User user = optionalUser.get();
        userService.updateUser(user, userEditRequest);
        userRepository.save(user);
        return new UserEditResponse(HttpStatus.OK, "OK", user.getAccountName());
    }
}
