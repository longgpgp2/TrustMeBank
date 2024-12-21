package com.trustme.service;

import java.util.*;

import com.trustme.dto.request.UserLoginRequest;
import com.trustme.dto.response.LoginResponse;
import com.trustme.dto.response.UserResponse;
import com.trustme.enums.ErrorCode;
import com.trustme.enums.Roles;
import com.trustme.model.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.trustme.dto.request.UserRegisterRequest;
import com.trustme.model.Role;
import com.trustme.model.User;
import com.trustme.repository.RoleRepository;
import com.trustme.repository.UserRepository;
/**
 * Service class for handling authentication-related operations.
 * This class contains methods for user registration, login, and other authentication functionalities.
 */
@Service
public class AuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CustomUserDetailsService userDetailsService;
    @Autowired
    KeyService keyService;
    @Autowired
    AuthenticationManager authenticationManager;
    /**
     * Registers a new user in the system.
     *
     * @param registerRequest the request object containing user registration details
     * @throws UserAlreadyExistsException if the user already exists
     */

    /**
     * @comment_by: toanlemanh
     * this method should be provided by UserService
     * does not make use of Mapper
     * t nghi minh ko can tao repo cho role vi minh gan role vao user luon
     * khong co user thi role cung ko ton tai duoc
     *
     *  cung cap gen token o auth service thi hop ly hon
     *
     */
    public void registerUser(UserRegisterRequest registerRequest) {
        // User user = UserMapper.INSTANCE.toUser (registerRequest);
        Optional<Role> role = roleRepository.findById(1L);
        System.out.println(registerRequest.toString());
        User user = User.builder()
                .username(registerRequest.getUsername())
                .accountName(registerRequest.getAccountName())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .balance(1000.0)
                .pinCode(registerRequest.getPinCode())
                .role(role.get())
                .build();
        userRepository.save(user);
    }


    public LoginResponse loginUser(UserLoginRequest loginRequest){
        try {
            CustomUserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                            loginRequest.getPassword()));
            if (userDetails != null) {
                List<String> authoritiesList = getJwtAuthoritiesFromRoles(userDetails);
                User user = userDetails.getUser();
                System.out.println(user.getUsername() + user.getPassword());
                String token = keyService.generateJwt(user.getUsername(), authoritiesList, 3600L);
                return new LoginResponse(HttpStatus.OK, "Login successful", token);
            }
        } catch (BadCredentialsException e) {
            ErrorCode invalidCredentials = ErrorCode.INVALID_CREDENTIALS;
            return new LoginResponse(invalidCredentials.getHttpStatus(), invalidCredentials.getErrorMessage(), null);
        } catch (UsernameNotFoundException e) {
            ErrorCode userNotFound = ErrorCode.USER_NOT_FOUND;
            return new LoginResponse(userNotFound.getHttpStatus(), userNotFound.getErrorMessage(), null);
        }
        ErrorCode invalidCredentials = ErrorCode.INVALID_CREDENTIALS;
        return new LoginResponse(invalidCredentials.getHttpStatus(), invalidCredentials.getErrorMessage(), null);
    }
    /**
     * Extract authorities from user's role
     * @param user authenticated user
     * @return a list of authorities (scopes) for jwt
     * */
    public List<String> getJwtAuthoritiesFromRoles(UserDetails user) {
        List<Roles> roles = new ArrayList<>();
        user.getAuthorities().stream().forEach(role -> {
            roles.add(Roles.fromString(role.getAuthority()));
        });
        Set<String> authorities = new HashSet<>();
        roles.stream().forEach(role -> authorities.addAll(role.getAuthorities()));
        List<String> authoritiesList = authorities.stream().toList();
        return authoritiesList;
    }
}
