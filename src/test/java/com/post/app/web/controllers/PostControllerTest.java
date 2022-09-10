package com.post.app.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.post.app.config.WithCustomUser;
import com.post.app.domain.Post;
import com.post.app.domain.User;
import com.post.app.services.JWTProvideService;
import com.post.app.web.model.BaseResponse;
import com.post.app.web.model.Post.PostDto;
import com.post.app.web.model.Post.PostListPaged;
import com.post.app.web.services.PostService;
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

@WebMvcTest(PostController.class)
@WithCustomUser(username = "John")  // Load custom security context with User object
class PostControllerTest {

    static Map<String, String> request;
    static String requestBody;

    @Autowired
    MockMvc mockMvc;
    @MockBean
    PostService postService;
    @MockBean
    JWTProvideService jwtProvideService;

    @BeforeAll
    static void setUp() throws Exception {
        request = new HashMap<>();
        request.put("title", "some title");
        request.put("content", "some content");
        requestBody = new ObjectMapper().writeValueAsString(request);
    }

    @AfterEach
    void tearDown() {
        reset(postService);
    }

    @Test
    void listPosts() throws Exception {
        given(postService.listPosts(any(), any())).willReturn(new PostListPaged());

        mockMvc.perform(get("/api/posts/list"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").hasJsonPath());

        then(postService).should().listPosts(any(), any());
    }

    @Test
    void create() throws Exception {
        given(postService.create(any(), any())).willReturn(new PostDto());

        mockMvc.perform(post("/api/posts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").hasJsonPath());

        then(postService).should().create(any(Post.class), any(User.class));
    }

    @Test
    void getPost() throws Exception {
        given(postService.findPost(any())).willReturn(new PostDto());

        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").hasJsonPath());

        then(postService).should().findPost(anyLong());
    }

    @Test
    void putPost() throws Exception {
        given(postService.putPost(any(), any(), any())).willReturn(new PostDto());

        mockMvc.perform(put("/api/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").hasJsonPath());

        then(postService).should().putPost(anyLong(), any(Post.class), any(User.class));
    }

    @Test
    void patchPost() throws Exception {
        given(postService.patchPost(any(), any(), any())).willReturn(new PostDto());

        mockMvc.perform(patch("/api/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").hasJsonPath());

        then(postService).should().patchPost(anyLong(), any(Post.class), any(User.class));
    }

    @Test
    void deletePost() throws Exception {
        given(postService.deleteById(any(), any())).willReturn(new BaseResponse(""));

        mockMvc.perform(delete("/api/posts/1")
                        .with(csrf()))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").hasJsonPath());

        then(postService).should().deleteById(anyLong(), any(User.class));
    }

    @Test
    void searchPosts() throws Exception {
        given(postService.findPostsByCategory(any(), any(), any(), any())).willReturn(new PostListPaged());

        mockMvc.perform(get("/api/posts/search?search=someQuery&category=TITLE")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").hasJsonPath());
    }
}