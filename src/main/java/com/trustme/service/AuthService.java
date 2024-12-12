package com.trustme.service;

import com.trustme.dto.request.UserRegisterRequest;
import com.trustme.model.Role;
import com.trustme.model.User;
import com.trustme.repository.RoleRepository;
import com.trustme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    public void registerUser(UserRegisterRequest registerRequest) {
//        User user = UserMapper.INSTANCE.toUser (registerRequest);
        Optional<Role> role = roleRepository.findById(Long.parseLong("1"));
        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(registerRequest.getPassword())
                .role(role.get()).build();
        userRepository.save(user);
    }
}
