package com.post.app.web.services;

import com.post.app.domain.Post;
import com.post.app.domain.User;
import com.post.app.model.ECategory;
import com.post.app.web.model.Post.PostDto;
import com.post.app.web.model.Post.PostListPaged;

public interface PostService {
    PostListPaged listPosts(Integer pageNumber, Integer pageSize);

    // CRUD operations
    PostDto findPost(Long postId);

    PostDto create(Post post, User user);

    void deleteById(Long postId, User user);

    PostDto putPost(Long postId, Post post, User user);

    PostDto patchPost(Long postId, Post post, User user);

    PostListPaged findPostsByCategory(String searchQuery, ECategory category, Integer pageNumber, Integer pageSize);
}
