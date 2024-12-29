package com.trustme;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trustme.dto.request.UserLoginRequest;
import com.trustme.dto.response.LoginResponse;
import com.trustme.dto.response.Response;
import com.trustme.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class TestingTest {
    @Autowired
    MockMvc mockMvc;
    String token;
    @Test
    void isAPIWorking() throws Exception {
        LoginResponse expectedResponse = new LoginResponse(200, "this is the test message","this is the test token" );
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/auth/test"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", Matchers.is(expectedResponse.getResult())))
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    void isLoginWorking() throws Exception {
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .username("user1")
                .password("123456")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginRequest)));

        MvcResult result = response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", Matchers.notNullValue()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        System.out.println(jsonResponse);
        LoginResponse loginResponse = objectMapper.readValue(jsonResponse, LoginResponse.class);
        token = loginResponse.getResult();
        System.out.println("Token:" + token);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/admin/users")
                .header("Authorization", "Bearer " + token))
                .andDo(MockMvcResultHandlers.print());
    }
}
