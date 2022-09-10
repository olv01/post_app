package com.post.app.web.services;

import com.post.app.domain.Post;
import com.post.app.domain.User;
import com.post.app.model.ECategory;
import com.post.app.repositories.PostRepository;
import com.post.app.web.model.BaseResponse;
import com.post.app.web.model.Post.PostDto;
import com.post.app.web.model.Post.PostListItem;
import com.post.app.web.model.Post.PostListPaged;
import com.post.app.web.model.mapper.PostMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @InjectMocks
    PostServiceImpl postService;

    @Mock
    PostRepository postRepository;

    @Mock
    PostMapper postMapper;

    User user = new User("John", "12345", "John@email.com", null);
    Post post = new Post("title", "content", user);

    @AfterEach
    void tearDown() {
        reset(postRepository);
        reset(postMapper);
    }

    @Test
    void listPosts() {
        Page<PostListItem> postListPage = new PageImpl<>(
                List.of(new PostListItem()),
                PageRequest.of(0, 20),
                1);
        given(postRepository.findPostListItem(any(Pageable.class))).willReturn(postListPage);

        PostListPaged postListPaged = postService.listPosts(0, 20);

        assertThat(postListPaged).isNotNull();
        then(postRepository).should().findPostListItem(any(Pageable.class));
    }

    @Test
    void findPost() {
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(postMapper.postToPostDto(any())).willReturn(new PostDto());

        PostDto postDto = postService.findPost(1L);

        assertThat(postDto).isNotNull();
        then(postRepository).should().findById(anyLong());
        then(postMapper).should().postToPostDto(any(Post.class));
    }

    @Test
    void create() {
        Post newPost = new Post("title", "content");
        given(postRepository.save(any())).willReturn(newPost);
        given(postMapper.postToPostDto(any())).willReturn(new PostDto());

        PostDto postDto = postService.create(newPost, user);

        assertThat(postDto).isNotNull();
        then(postRepository).should().save(any(Post.class));
        then(postMapper).should().postToPostDto(any(Post.class));
    }

    @Test
    void deleteById() {
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        BaseResponse baseResponse = postService.deleteById(1L, user);

        assertThat(baseResponse).isNotNull();
        then(postRepository).should().findById(1L);
        then(postRepository).should().delete(any(Post.class));
    }

    @Test
    void putPost() {
        Post newPost = new Post("new title", "new content");
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(postRepository.save(any())).willReturn(newPost);
        given(postMapper.postToPostDto(any())).willReturn(new PostDto());

        PostDto postDto = postService.putPost(1L, newPost, user);

        assertThat(postDto).isNotNull();
        then(postRepository).should().findById(1L);
        then(postRepository).should().save(any(Post.class));
        then(postMapper).should().postToPostDto(any(Post.class));
    }

    @Test
    void patchPost() {
        Post newPost = new Post("new title", "new content");

        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(postRepository.save(any())).willReturn(newPost);
        given(postMapper.postToPostDto(any())).willReturn(new PostDto());

        PostDto postDto = postService.patchPost(1L, newPost, user);

        assertThat(postDto).isNotNull();
        then(postRepository).should().findById(1L);
        then(postRepository).should().save(any(Post.class));
        then(postMapper).should().postToPostDto(any(Post.class));
    }

    @Test
    void findPostsByCategory() {
        Page<PostListItem> postListPage = new PageImpl<>(
                List.of(new PostListItem()),
                PageRequest.of(0, 20),
                1);
        given(postRepository.findPostListDtoByTitle(any(), any())).willReturn(postListPage);

        PostListPaged postListPaged = postService.findPostsByCategory("query", ECategory.TITLE, 0, 20);

        assertThat(postListPaged).isNotNull();
        then(postRepository).should().findPostListDtoByTitle(any(Pageable.class), anyString());
    }
}