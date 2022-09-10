package com.post.app.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.post.app.services.JWTProvideService;
import com.post.app.web.model.auth.*;
import com.post.app.web.services.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@WithMockUser // Needed to pass security filter
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthenticationService authenticationService;

    @MockBean
    JWTProvideService jwtProvideService;

    @Test
    void signIn() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("username", "John");
        request.put("password", "12345");

        JWTResponse response = new JWTResponse("Login Successful",
                "John",
                "excryptedToken",
                new Date());
        given(authenticationService.authenticateUser(any())).willReturn(response);

        mockMvc.perform(post("/api/auth/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonToString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("John"))
                .andExpect(jsonPath("$.token").isNotEmpty());

        then(authenticationService).should().authenticateUser(any(SignInRequest.class));
    }

    @Test
    void signUp() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("username", "Mike");
        request.put("password", "12345");
        request.put("email", "Mike@email.com");

        JWTResponse response = new JWTResponse("New User Created",
                "Mike",
                "encryptedToken",
                new Date());
        given(authenticationService.createNewUser(any())).willReturn(response);

        mockMvc.perform(post("/api/auth/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonToString(request))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("Mike"))
                .andExpect(jsonPath("$.token").isNotEmpty());

        then(authenticationService).should().createNewUser(any(SignUpRequest.class));
    }

    @Test
    void checkUsername() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("username", "John");
        UserCheckResponse response = new UserCheckResponse("", true);
        given(authenticationService.checkUsername(any())).willReturn(response);

        mockMvc.perform(post("/api/auth/checkUsername")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonToString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exist").value(true));

        then(authenticationService).should().checkUsername(any(UserCheckRequest.class));
    }

    private String asJsonToString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}