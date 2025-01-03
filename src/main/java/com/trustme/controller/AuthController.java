package com.trustme.controller;

import com.trustme.dto.UserDto;
import com.trustme.dto.response.UserResponse;
import com.trustme.enums.StatusCode;
import jakarta.servlet.http.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.trustme.dto.request.UserLoginRequest;
import com.trustme.dto.request.UserRegisterRequest;
import com.trustme.dto.response.LoginResponse;
import com.trustme.service.AuthService;
import com.trustme.service.CustomUserDetailsService;

import java.net.URI;
import java.net.URL;

@RestController()
@RequestMapping("/auth")
public class AuthController {
    private final CustomUserDetailsService userDetailsService;
    private final AuthService authService;

    /**
     * As of Spring Framework 4.3, an @Autowired annotation on such a constructor is no longer necessary if the target
     * bean defines only one constructor to begin with. However, if several constructors are available and there is no
     * primary/default constructor, at least one of the constructors must be annotated with @Autowired in order to
     * instruct the container which one to use.
     */
    public AuthController(CustomUserDetailsService userDetailsService, AuthService authService) {
        this.userDetailsService = userDetailsService;
        this.authService = authService;
    }


    @GetMapping("/register")
    public ResponseEntity<String> getRegister() {
        return new ResponseEntity<String>("Hello, please enter your credentials", HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> postRegister(@RequestBody UserRegisterRequest registerRequest) {
            UserResponse userResponse = authService.registerUser(registerRequest);
            return ResponseEntity.status(userResponse.getCode()).body(userResponse);
    }

    @GetMapping("/login")
    public ResponseEntity<String> getLogin(@RequestParam(required = false) Boolean error) {
        if(error != null){
            return new ResponseEntity<String>("Invalid credentials, please try again!", HttpStatus.OK);
        }
        return new ResponseEntity<String>("Hello, please enter your credentials", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> postLogin(@RequestBody UserLoginRequest loginRequest) {
        LoginResponse loginResponse = authService.loginUser(loginRequest);

        ResponseCookie jwtCookie = ResponseCookie.from("jwt-token", loginResponse.getResult())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();

        URI nextStep = URI.create("/api/home");

        return ResponseEntity.status(
                loginResponse.getCode())
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .location(nextStep)
                .body(loginResponse);
    }

    @GetMapping("/test")
    public ResponseEntity<LoginResponse> testAPI(){
        return ResponseEntity.ok().body(new LoginResponse(200, "this is the test message","this is the test token" ));
    }

    @GetMapping("/info")
    public ResponseEntity<UserResponse> getUserInfo(){
        UserDto user = authService.getCurrentUserDto();
        UserResponse userResponse = new UserResponse(
                    StatusCode.OK.getHttpStatus(),
                    StatusCode.OK.getStatusMessage(),
                    user);
        return ResponseEntity.status(userResponse.getCode()).body(userResponse);
    }

}
