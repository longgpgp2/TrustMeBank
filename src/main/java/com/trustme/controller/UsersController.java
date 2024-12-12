package com.trustme.controller;

import com.trustme.dto.request.UserEditRequest;
import com.trustme.dto.response.ErrorResponse;
import com.trustme.dto.response.UserEditResponse;
import com.trustme.dto.response.UserResponse;
import com.trustme.dto.response.UsersResponse;
import com.trustme.mapper.UserMapper;
import com.trustme.model.User;
import com.trustme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class UsersController {
    UserMapper userMapper;
    @Autowired
    UserRepository userRepository;
    @GetMapping("/users")
    public UsersResponse getUsers(){
        List<User> users = userRepository.findAll();
        return new UsersResponse(HttpStatus.OK, "OK", users);
    }
    @GetMapping("/user")
    public UserResponse getUser(@RequestParam String accountName){
        Optional<User> user = userRepository.findByAccountName(accountName);
        return user.map(value -> {
            return new UserResponse(HttpStatus.OK, "OK", value);
        }).orElseGet(() -> new UserResponse(HttpStatus.NOT_ACCEPTABLE, "Invalid user", null));
    }
    @PostMapping("/user")
    public UserEditResponse postUser(@RequestBody UserEditRequest userEditRequest){
        Optional<User> optionalUser = userRepository.findByAccountName(userEditRequest.getAccountName());
        if(optionalUser.isEmpty())
            return new UserEditResponse(HttpStatus.NOT_ACCEPTABLE, "Invalid user", null);
        User user = userMapper.toUser(userEditRequest);
        userRepository.save(user);
        return new UserEditResponse(HttpStatus.OK, "OK", user.getAccountName());
    }
}
