package com.trustme.controller;

import java.util.List;

import com.trustme.service.KeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trustme.dto.request.UserLoginRequest;
import com.trustme.dto.request.UserRegisterRequest;
import com.trustme.dto.response.LoginResponse;
import com.trustme.model.CustomUserDetails;
import com.trustme.model.User;
import com.trustme.service.AuthService;
import com.trustme.service.CustomUserDetailsService;

@RestController()
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    CustomUserDetailsService userDetailsService;
    @Autowired
    AuthService authService;
    @Autowired
    KeyService keyService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public ResponseEntity<String> home() {
        return new ResponseEntity<String>("message", HttpStatus.OK);
    }

    @GetMapping("/register")
    public ResponseEntity<String> getRegister() {
        return new ResponseEntity<String>("Hello, please enter your credentials", HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> postRegister(@RequestBody UserRegisterRequest registerRequest) {
        UserDetails user = userDetailsService.loadUserByUsername(registerRequest.getUsername());
        if (user != null) {
            return new ResponseEntity<String>("User already exists", HttpStatus.NOT_ACCEPTABLE);
        }
        authService.registerUser(registerRequest);
        return new ResponseEntity<String>("User created successfully", HttpStatus.OK);
    }

    @GetMapping("/login")
    public ResponseEntity<String> getLogin() {
        return new ResponseEntity<String>("Hello, please enter your credentials", HttpStatus.OK);
    }

    @PostMapping("/login")
    public LoginResponse postLogin(@RequestBody UserLoginRequest loginRequest) {
        try {
            CustomUserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                            loginRequest.getPassword()));
            if (userDetails != null) {
                List<String> authoritiesList = authService.getJwtAuthoritiesFromRoles(userDetails);
                User user = userDetails.getUser();
                System.out.println(user.getUsername() + user.getPassword());
                String token = keyService.generateJwt(user.getUsername(), authoritiesList, 3600L);
                return new LoginResponse(HttpStatus.OK, "Login successful", token);
            }
        } catch (BadCredentialsException e) {
            return new LoginResponse(HttpStatus.UNAUTHORIZED, "Incorrect username or password", null);
        } catch (UsernameNotFoundException e) {
            new LoginResponse(HttpStatus.UNAUTHORIZED, "User not found", null);
        }
        return new LoginResponse(HttpStatus.UNAUTHORIZED, "Invalid credentials", null);
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
