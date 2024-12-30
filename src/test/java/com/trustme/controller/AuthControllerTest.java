package com.trustme.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trustme.dto.request.UserLoginRequest;
import com.trustme.dto.request.UserRegisterRequest;
import com.trustme.dto.response.LoginResponse;
import com.trustme.service.AuthService;
import com.trustme.service.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    String token;
    UserLoginRequest loginRequest;
    UserRegisterRequest registerRequest;
    @BeforeEach
    void initData() throws Exception {
        loginRequest = UserLoginRequest.builder()
                .username("user3")
                .password("123456")
                .build();
        registerRequest = UserRegisterRequest.builder()
                .accountName("user3")
                .username("user3")
                .pinCode("1234")
                .password("123456")
                .build();

    }

    @Test
    void isRegisterWorking() throws Exception {
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)));

        MvcResult result = response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());

    }
    @Test
    void isLoginWorking() throws Exception {
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));

        MvcResult result = response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", Matchers.notNullValue()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        System.out.println(jsonResponse);
        LoginResponse loginResponse = objectMapper.readValue(jsonResponse, LoginResponse.class);
        token = loginResponse.getResult();
    }
}
