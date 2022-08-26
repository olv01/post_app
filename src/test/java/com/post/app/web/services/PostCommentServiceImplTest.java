package com.post.app.web.services;

import com.post.app.domain.Post;
import com.post.app.domain.PostComment;
import com.post.app.domain.User;
import com.post.app.repositories.PostCommentRepository;
import com.post.app.repositories.PostRepository;
import com.post.app.web.model.BaseResponse;
import com.post.app.web.model.Post.PostCommentDto;
import com.post.app.web.model.mapper.PostCommentMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;

@ExtendWith(MockitoExtension.class)
class PostCommentServiceImplTest {

    @InjectMocks
    PostCommentServiceImpl postCommentService;

    @Mock
    PostRepository postRepository;

    @Mock
    PostCommentRepository postCommentRepository;

    @Mock
    PostCommentMapper postCommentMapper;

    User user = new User("John", "12345", "John@email.com", null);
    Post post = new Post("title", "content", user);
    PostComment postComment = new PostComment("content");

    @AfterEach
    void tearDown() {
        reset(postCommentRepository);
        reset(postCommentMapper);
        ;
    }

    @Test
    void create() {
        PostComment newPostComment = new PostComment("new content");
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(postCommentRepository.save(any())).willReturn(postComment);
        given(postCommentMapper.postCommentToPostCommentDto(any())).willReturn(new PostCommentDto());

        PostCommentDto postCommentDto = postCommentService.create(1L, newPostComment, user);

        assertThat(postCommentDto).isNotNull();
        then(postRepository).should().findById(anyLong());
        then(postCommentRepository).should().save(any(PostComment.class));
        then(postCommentMapper).should().postCommentToPostCommentDto(any(PostComment.class));
    }

    @Test
    void deleteById() {
        postComment.setUser(user);
        given(postCommentRepository.findById(anyLong())).willReturn(Optional.of(postComment));

        BaseResponse baseResponse = postCommentService.deleteById(1L, user);

        assertThat(baseResponse).isNotNull();
        then(postCommentRepository).should().findById(anyLong());
        then(postCommentRepository).should().deleteById(anyLong());
    }

    @Test
    void update() {
        postComment.setUser(user);
        PostComment newPostComment = new PostComment("new content");
        given(postCommentRepository.findById(anyLong())).willReturn(Optional.of(postComment));
        given(postCommentRepository.save(any())).willReturn(postComment);
        given(postCommentMapper.postCommentToPostCommentDto(any())).willReturn(new PostCommentDto());

        PostCommentDto postCommentDto = postCommentService.update(1L, newPostComment, user);

        assertThat(postCommentDto).isNotNull();

        then(postCommentRepository).should().findById(anyLong());
        then(postCommentRepository).should().save(any(PostComment.class));
        then(postCommentMapper).should().postCommentToPostCommentDto(any(PostComment.class));
    }
}