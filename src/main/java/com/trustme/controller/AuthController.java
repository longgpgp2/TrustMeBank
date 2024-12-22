package com.trustme.controller;

import java.util.List;

import com.trustme.constant.ConstantResponses;
import com.trustme.dto.response.UserResponse;
import com.trustme.exception.exceptions.UsernameNotAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
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
    @Autowired
    CustomUserDetailsService userDetailsService;
    @Autowired
    AuthService authService;

    @GetMapping("/")
    public ResponseEntity<String> home() {
        return new ResponseEntity<String>("message", HttpStatus.OK);
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
    public UserResponse postRegister(@RequestBody UserRegisterRequest registerRequest) {
        try{
            UserDetails user = userDetailsService.loadUserByUsername(registerRequest.getUsername());
            if (user != null) {
                throw new UsernameNotAvailableException();
            }
            return authService.registerUser(registerRequest);
        }
        catch (UsernameNotAvailableException e){
            return ConstantResponses.USER_EXISTED;
        }

    }

    @GetMapping("/login")
    public ResponseEntity<String> getLogin() {
        return new ResponseEntity<String>("Hello, please enter your credentials", HttpStatus.OK);
    }

    @PostMapping("/login")
    public LoginResponse postLogin(@RequestBody UserLoginRequest loginRequest) {
        return authService.loginUser(loginRequest);
    }

    @GetMapping("/info")
    public String getUserInfo(@AuthenticationPrincipal Jwt jwt) {
        if (jwt != null) {
            List<String> scope = jwt.getClaim("scope");

            if (scope != null && !scope.isEmpty()) {
                System.out.println("Scopes: " + String.join(", ", scope));
                return "Authenticated User with scopes: " + String.join(", ", scope);
            } else {
                return "Authenticated User, but no scopes found.";
            }
        } else {
            return "No user authenticated";
        }
    }
}
