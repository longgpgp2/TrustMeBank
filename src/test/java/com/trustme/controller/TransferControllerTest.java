package com.trustme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trustme.dto.request.UserLoginRequest;
import com.trustme.dto.request.UserRegisterRequest;
import com.trustme.service.AuthService;
import com.trustme.service.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class TransferControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;
    @MockitoBean
    private AuthService authService;
    UserLoginRequest userLoginRequest;
    String token;
    @BeforeEach
    void initData(){
        userLoginRequest = UserLoginRequest.builder()
                .username("user1")
                .password("123456")
                .build();


    }
    @Test
    void shouldGenerateTokenOnLogin() throws Exception {
        // Prepare the login request data
        UserLoginRequest userLoginRequest = new UserLoginRequest("user1", "123456");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(userLoginRequest);

        // Perform the login request and expect a valid token in response
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk()) // Expect HTTP 200
                .andExpect(MockMvcResultMatchers.jsonPath("$.access_token").exists()) // Token should be present
                .andExpect(MockMvcResultMatchers.jsonPath("$.token_type").value("bearer")); // Token type should be "bearer"
    }

    @Test
    void isUserLoggedInSuccessfully() throws Exception {
        log.info("User logging in");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(userLoginRequest);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
        String responseContent = result.getResponse().getContentAsString();
        log.info("Response content: {}", responseContent);
//
//        LoginResponse loginResponse = authService.loginUser(userLoginRequest);
//        assert loginResponse.getCode().equals(HttpStatusCode.valueOf(200));
    }
}
