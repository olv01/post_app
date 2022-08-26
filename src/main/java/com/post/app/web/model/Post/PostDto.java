package com.post.app.web.model.Post;

import java.sql.Timestamp;
import java.util.List;

public class PostDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private Timestamp createdDate;
    private Timestamp lastModifiedDate;
    private List<PostCommentDto> postCommentDtos;

    public PostDto() {
    }

    public PostDto(Long id,
                   String title,
                   String content,
                   String username,
                   Timestamp createdDate,
                   Timestamp lastModifiedDate,
                   List<PostCommentDto> postCommentDtos) {

        this.id = id;
        this.title = title;
        this.content = content;
        this.username = username;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.postCommentDtos = postCommentDtos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Timestamp lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public List<PostCommentDto> getPostCommentDtos() {
        return postCommentDtos;
    }

    public void setPostCommentDtos(List<PostCommentDto> postCommentDtos) {
        this.postCommentDtos = postCommentDtos;
    }
}
