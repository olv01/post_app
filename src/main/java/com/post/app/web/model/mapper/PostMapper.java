package com.post.app.web.model.mapper;

import com.post.app.domain.Post;
import com.post.app.web.model.Post.PostDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {PostCommentMapper.class})
public interface PostMapper {
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "postComments", target = "postCommentDtos")
    PostDto postToPostDto(Post post);
}
