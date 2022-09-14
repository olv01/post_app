package com.post.app.repositories;

import com.post.app.domain.Post;
import com.post.app.domain.User;
import com.post.app.web.model.Post.PostListItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql({"/db/init_role.sql", "/db/init_user.sql"})
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    User user;
    PageRequest page;

    // Because @DataJpaTest is @Transactional, it's rolled back after each test
    @BeforeEach
    void setUp() {
        user = userRepository.findByUsername("John").get();
        Post post1 = new Post("title #1", "content #1", user);
        postRepository.save(post1);

        page = PageRequest.of(0, 20, Sort.by("createdDate").descending());
    }

    @Test
    void save() {
        Post post = new Post("new post", "new post content", user);

        Post saved = postRepository.save(post);

        assertThat(saved).isNotNull();
        assertThat(saved.getUser().getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void findPostListItem() {
        Page<PostListItem> postListPage = postRepository.findPostListItem(page);

        assertThat(postListPage.getTotalElements()).isNotZero();
        assertThat(postListPage.getContent().get(0)).isNotNull();
    }

    @Test
    void findPostListDtoByTitle() {
        Page<PostListItem> postListPage = postRepository.findPostListDtoByTitle(page, "title");

        assertThat(postListPage.getTotalElements()).isNotZero();
        assertThat(postListPage.getContent().get(0)).isNotNull();
    }

    @Test
    void findPostListDtoByContent() {
        Page<PostListItem> postListPage = postRepository.findPostListDtoByContent(page, "content");

        assertThat(postListPage.getTotalElements()).isNotZero();
        assertThat(postListPage.getContent().get(0)).isNotNull();
    }
}