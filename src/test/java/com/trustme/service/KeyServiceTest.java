package com.trustme.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class KeyServiceTest {

    private AuthService authService;

    @BeforeEach
    void setUp(){
        authService = new AuthService();
    }
    @Test
    void isTokenGenerated(){
        log.info("Start creating token");
        String jwt = authService.generateJwt("user", null, 3600L);
        assert jwt!=null;
    }
    @Test
    void contextLoads() {
        assert 1!=2;

    }
}
