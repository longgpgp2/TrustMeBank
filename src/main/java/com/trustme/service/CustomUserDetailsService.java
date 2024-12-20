package com.trustme.service;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.trustme.model.CustomUserDetails;
import com.trustme.model.User;
import com.trustme.repository.UserRepository;

import lombok.RequiredArgsConstructor;
@Slf4j
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading a user.");
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent())
            return new CustomUserDetails(user.get());
        return null;
    }

}
