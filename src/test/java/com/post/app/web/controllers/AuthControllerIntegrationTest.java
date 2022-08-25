package com.post.app.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"dev", "init-role", "init-user"})
class AuthControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void signIn() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("username", "John");
        request.put("password", "12345");
        String requestBody = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/api/auth/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("John"))
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Login Successful"));
    }

    @Test
    void signUp() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("username", "Mike");
        request.put("password", "12345");
        request.put("email", "Mike@email.com");
        String requestBody = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/api/auth/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("Mike"))
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.message").value("New User Created"));

    }
}