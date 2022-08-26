package com.post.app.web.model.mapper;

import com.post.app.domain.PostComment;
import com.post.app.web.model.Post.PostCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PostCommentMapper {
    @Mapping(source = "user.username", target = "username")
    PostCommentDto postCommentToPostCommentDto(PostComment postComment);
}
