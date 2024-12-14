package com.trustme.controller;

import com.trustme.utils.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class TokenController {

    @Autowired
    KeyGenerator keyGenerator;
    @GetMapping("/generate-token")
    public String generateToken(@RequestParam String username) {
        List<String> scopes = Arrays.asList("home:read", "admin:read");
        return keyGenerator.generateJwt(username, scopes, 3600L);
    }

}
