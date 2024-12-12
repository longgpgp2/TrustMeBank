package com.trustme.controller;

import com.trustme.dto.request.UserLoginRequest;
import com.trustme.dto.request.UserRegisterRequest;
import com.trustme.dto.response.LoginResponse;
import com.trustme.service.AuthService;
import com.trustme.service.CustomUserDetailsService;
import com.trustme.utils.AuthUtils;
import com.trustme.utils.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController()
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    CustomUserDetailsService userDetailsService;
    @Autowired
    AuthService authService;
    @Autowired
    KeyGenerator keyGenerator;


    @GetMapping("/")
    public ResponseEntity<String> home(){
        return new ResponseEntity<String>("message", HttpStatus.OK);
    }
    @GetMapping("/register")
    public ResponseEntity<String> getRegister() {
        return new ResponseEntity<String>("Hello, please enter your credentials", HttpStatus.OK);
    }
    @PostMapping("/register")
    public ResponseEntity<String> postRegister(@RequestBody UserRegisterRequest registerRequest){
        UserDetails user = userDetailsService.loadUserByUsername(registerRequest.getUsername());
        if(user!=null) {
            return new ResponseEntity<String>("User already exists", HttpStatus.NOT_ACCEPTABLE);
        }
        authService.registerUser(registerRequest);
        return new ResponseEntity<String>("User created successfully", HttpStatus.OK);
    }


    @GetMapping("/login")
    public ResponseEntity<String> getLogin(){
        return new ResponseEntity<String>("Hello, please enter your credentials", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> postLogin(@RequestBody UserLoginRequest loginRequest){
        UserDetails user = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        if(user!=null) {
            List<String> authoritiesList = AuthUtils.getJwtAuthoritiesFromRoles(user);
            String token = keyGenerator.generateJwt(user.getUsername(), authoritiesList, 3600L);
            return ResponseEntity.ok(new LoginResponse(HttpStatus.OK, "Login successful", token));
        }  else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(HttpStatus.UNAUTHORIZED, "Invalid credentials", null));
    }
}
