package com.trustme.controller;

import com.trustme.constant.ConstantResponses;
import com.trustme.dto.response.UserResponse;
import com.trustme.exception.exceptions.UsernameNotAvailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trustme.dto.request.UserLoginRequest;
import com.trustme.dto.request.UserRegisterRequest;
import com.trustme.dto.response.LoginResponse;
import com.trustme.service.AuthService;
import com.trustme.service.CustomUserDetailsService;

@RestController()
@RequestMapping("/auth")
public class AuthController {
    private final CustomUserDetailsService userDetailsService;
    private final AuthService authService;

    public AuthController(CustomUserDetailsService userDetailsService, AuthService authService) {
        this.userDetailsService = userDetailsService;
        this.authService = authService;
    }


    @GetMapping("/register")
    public ResponseEntity<String> getRegister() {
        return new ResponseEntity<String>("Hello, please enter your credentials", HttpStatus.OK);
    }

    /**
     *
     * @comment_by: toanlemanh
     *
     *  minh co the tao enum cac loi thuong gap
     *  ben trong controller dang xu ly nhieu logic qua, chu nen dispatch cho service roi
     *  gui response tuong ung
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> postRegister(@RequestBody UserRegisterRequest registerRequest) {
        System.out.println(registerRequest.getUsername() + ", " + registerRequest.getPassword() + ", " + registerRequest.getAccountName() + ", " +registerRequest.getPinCode());
        try{
            userDetailsService.loadUserByUsername(registerRequest.getUsername());
        }
        catch (UsernameNotFoundException e){
            UserResponse userResponse = authService.registerUser(registerRequest);
            return ResponseEntity.status(userResponse.getCode()).body(userResponse);
        }
        return ResponseEntity.status(ConstantResponses.USER_EXISTED.getCode()).body(ConstantResponses.USER_EXISTED);

    }

    @GetMapping("/login")
    public ResponseEntity<String> getLogin() {
        return new ResponseEntity<String>("Hello, please enter your credentials", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> postLogin(@RequestBody UserLoginRequest loginRequest) {
        LoginResponse loginResponse = authService.loginUser(loginRequest);
        return ResponseEntity.status(loginResponse.getCode()).body(loginResponse);
    }

    @GetMapping("/test")
    public ResponseEntity<LoginResponse> testAPI(){
        return ResponseEntity.ok().body(new LoginResponse(200, "this is the test message","this is the test token" ));
    }

}
