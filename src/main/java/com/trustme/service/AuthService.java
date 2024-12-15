package com.trustme.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.trustme.dto.request.UserRegisterRequest;
import com.trustme.model.Role;
import com.trustme.model.User;
import com.trustme.repository.RoleRepository;
import com.trustme.repository.UserRepository;

@Service
public class AuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

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
//    public Jwt getCurrentLoggedInUser(@AuthenticationPrincipal Jwt jwt){
//        return jwt;
//    }
}
