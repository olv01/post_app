package com.post.app.repositories;

import com.post.app.domain.Post;
import com.post.app.web.model.Post.PostListItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT new com.post.app.web.model.Post.PostListItem(" +
            "p.id, p.title, u.username, p.createdDate, p.lastModifiedDate, count(pc.id)" +
            ") FROM Post p " +
            "JOIN p.user u " +
            "LEFT JOIN p.postComments pc " +
            "GROUP BY p")
    Page<PostListItem> findPostListItem(Pageable pageable);
}
