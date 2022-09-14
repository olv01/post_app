package com.post.app.repositories;

import com.post.app.domain.Post;
import com.post.app.domain.PostComment;
import com.post.app.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql({"/db/init_role.sql", "/db/init_user.sql"})
class PostCommentRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostCommentRepository postCommentRepository;

    @Autowired
    UserRepository userRepository;

    User user1;
    User user2;
    Post post;
    Post savedPost;
    PostComment savedPostComment;

    // Because @DataJpaTest is @Transactional, it's rolled back after each test
    @BeforeEach
    void setUp() {
        user1 = userRepository.findByUsername("John").get();
        user2 = userRepository.findByUsername("Jane").get();
        post = new Post("title #1", "content #1", user1);

        savedPost = postRepository.save(post);

        PostComment postComment = new PostComment("some comment");
        postComment.setPost(savedPost);
        postComment.setUser(user1);

        savedPostComment = postCommentRepository.save(postComment);
    }

    @Test
    void findById() {
        Long id = savedPostComment.getId();

        PostComment postComment = postCommentRepository.findById(id).get();

        assertThat(postComment).isNotNull();
        assertThat(postComment.getContent()).isEqualTo("some comment");
    }

    @Test
    void save() {
        PostComment newPostComment = new PostComment("new comment");
        newPostComment.setPost(post);
        newPostComment.setUser(user2);

        PostComment saved = postCommentRepository.save(newPostComment);

        assertThat(saved).isNotNull();
        assertThat(saved.getContent()).isEqualTo("new comment");
        assertThat(saved.getUser().getUsername()).isEqualTo(user2.getUsername());
    }

    @Test
    void deleteById() {
        Long id = savedPostComment.getId();
        postCommentRepository.deleteById(id);

        Optional<PostComment> postComment = postCommentRepository.findById(id);
        assertThat(postComment.isPresent()).isFalse();
    }
}