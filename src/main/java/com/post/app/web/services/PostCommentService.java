package com.post.app.web.services;

import com.post.app.domain.PostComment;
import com.post.app.domain.User;
import com.post.app.web.model.Post.PostCommentDto;

public interface PostCommentService {
    PostCommentDto create(Long postId, PostComment postComment, User user);

    void deleteById(Long postCommentId, User user);

    PostCommentDto update(Long postCommentId, PostComment postComment, User user);
}
