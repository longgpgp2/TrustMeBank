package com.trustme.service;

import java.util.*;

import com.trustme.enums.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    /**
     * Registers a new user in the system.
     *
     * @param registerRequest the request object containing user registration details
     * @throws UserAlreadyExistsException if the user already exists
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
