package com.trustme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trustme.dto.request.TransferRequest;
import com.trustme.dto.request.UserLoginRequest;
import com.trustme.dto.response.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class TransferControllerTest {
    @Autowired
    private MockMvc mockMvc;
    UserLoginRequest userLoginRequest;
    ObjectMapper objectMapper = new ObjectMapper();
    String token;
    @BeforeEach
    void initToken() throws Exception{
        userLoginRequest = UserLoginRequest.builder()
                .username("user1")
                .password("123456")
                .build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userLoginRequest)))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        LoginResponse loginResponse = objectMapper.readValue(jsonResponse, LoginResponse.class);
        token = loginResponse.getResult();

    }
    @Test
    void shouldReturnATransferAfterTransfering() throws Exception {
        log.info("Testing transfer");
        TransferRequest transferRequest = TransferRequest.builder()
                .amount(100.0)
                .description("No Desc")
                .receiver("user2")
                .build();
        String content = objectMapper.writeValueAsString(transferRequest);
        String redirectedUrl = performPost(content, "/api/transfer").andReturn().getResponse().getRedirectedUrl();
        String step2Url =  performPost(content, redirectedUrl).andReturn().getResponse().getRedirectedUrl();
        MvcResult response =  performPost(content, step2Url)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        System.out.println(response.getResponse().getContentAsString());

    }

    @Test
    void shouldReturnAllTransfers() throws Exception {
        log.info("Retrieving all transfers");
        MvcResult response = performGet("/admin/transfers")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println(response.getResponse().getContentAsString());

    }

    public ResultActions performPost(String content, String url) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
    }
    public ResultActions performGet(String url) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .header("Authorization", "Bearer " + token));
    }
}
