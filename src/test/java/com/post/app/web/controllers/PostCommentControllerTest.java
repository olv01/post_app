package com.post.app.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.post.app.config.WithCustomUser;
import com.post.app.domain.PostComment;
import com.post.app.domain.User;
import com.post.app.services.JWTProvideService;
import com.post.app.web.model.BaseResponse;
import com.post.app.web.model.Post.PostCommentDto;
import com.post.app.web.services.PostCommentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostCommentController.class)
@WithCustomUser(username = "John")
class PostCommentControllerTest {

    static Map<String, String> request;
    static String requestBody;
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostCommentService postCommentService;

    @MockBean
    JWTProvideService jwtProvideService;

    @BeforeAll
    static void setUp() throws Exception {
        request = new HashMap<>();
        request.put("content", "some content");
        requestBody = new ObjectMapper().writeValueAsString(request);
    }

    @AfterEach
    void tearDown() throws Exception {
        reset(postCommentService);
    }

    @Test
    void createPostComment() throws Exception {
        given(postCommentService.create(any(), any(), any())).willReturn(new PostCommentDto());

        mockMvc.perform(post("/api/posts/1/comments/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").hasJsonPath());

        then(postCommentService).should().create(anyLong(), any(PostComment.class), any(User.class));
    }

    @Test
    void deletePostComment() throws Exception {
        given(postCommentService.deleteById(any(), any())).willReturn(new BaseResponse(""));

        mockMvc.perform(delete("/api/posts/1/comments/1")
                        .with(csrf()))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").hasJsonPath());

        then(postCommentService).should().deleteById(anyLong(), any(User.class));
    }

    @Test
    void patchPostComment() throws Exception {
        given(postCommentService.update(any(), any(), any())).willReturn(new PostCommentDto());

        mockMvc.perform(patch("/api/posts/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").hasJsonPath());

        then(postCommentService).should().update(anyLong(), any(PostComment.class), any(User.class));
    }
}