package com.trustme.service;

import java.time.LocalDateTime;
import java.util.*;

import com.trustme.dto.UserDto;
import com.trustme.dto.request.UserLoginRequest;
import com.trustme.dto.response.LoginResponse;
import com.trustme.dto.response.UserResponse;
import com.trustme.enums.StatusCode;
import com.trustme.exception.exceptions.ResourceNotAvailableException;
import com.trustme.exception.exceptions.ResourceNotFoundException;
import com.trustme.dto.mapper.CustomUserMapper;
import com.trustme.model.CustomUserDetails;
import com.trustme.util.JwtUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.trustme.dto.request.UserRegisterRequest;
import com.trustme.model.Role;
import com.trustme.model.User;
import com.trustme.repository.RoleRepository;
import com.trustme.repository.UserRepository;
/**
 * Service class for handling authentication-related and jwt encoding operations.
 * This class contains methods for generating signing keys and jwt, user registration, login, and other authentication functionalities.
 */
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, CustomUserDetailsService userDetailsService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Registers a new user in the system.
     *
     * @param registerRequest the request object containing user registration details
     */
    public UserResponse registerUser(UserRegisterRequest registerRequest) {
        validateRegisterRequest(registerRequest);
        Long roleId = 1L;
        Role role = roleRepository.findById(roleId)
                .orElseThrow(()-> new ResourceNotFoundException("Role not found" + roleId));
        User user = User.builder()
                .username(registerRequest.getUsername())
                .fullName(registerRequest.getFullName())
                .accountName(registerRequest.getAccountName())
                .email(registerRequest.getEmail())
                .phone(registerRequest.getPhone())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .balance(1000.0)
                .createdAt(LocalDateTime.now())
                .pinCode(registerRequest.getPinCode())
                .role(role)
                .build();

        userRepository.save(user);
        return new UserResponse(StatusCode.CREATED.getHttpStatus(),StatusCode.CREATED.getStatusMessage(), CustomUserMapper.getUserDto(user));
    }


    public LoginResponse loginUser(UserLoginRequest loginRequest){
        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));
            List<String> authoritiesList = jwtUtil.getJwtAuthoritiesFromRoles(userDetails);
            User user = userDetails.getUser();
            String token = jwtUtil.generateJwt(user.getUsername(), authoritiesList, 3600L);
        return new LoginResponse(200, "Login successful", token);
    }

    /**
     * Retrieve the current logged in userDto
     *
     * @return UserDto of the current user
     */
    public UserDto getCurrentUserDto(){
        return CustomUserMapper.getUserDto(getCurrentUser());
    }

    /**
     * Retrieve the current logged in user
     *
     * @return current User
     */
    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        return userRepository.findByUsername(jwt.getSubject())
                .orElseThrow(()-> new ResourceNotAvailableException("Login required!"));
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public void validateRegisterRequest(UserRegisterRequest userRegisterRequest){
        if (userRepository.existsByUsername(userRegisterRequest.getUsername())) {
            throw new ResourceNotAvailableException("Username already exists");
        }

        if (userRepository.existsByAccountName(userRegisterRequest.getUsername())) {
            throw new ResourceNotAvailableException("Account Name already exists");
        }

        if (userRepository.existsByEmail(userRegisterRequest.getEmail())) {
            throw new ResourceNotAvailableException("Email already exists");
        }

        if (userRepository.existsByPhone(userRegisterRequest.getPhone())) {
            throw new ResourceNotAvailableException("Phone number already exists");
        }
    }

    public void validateUser(){

    }
}
