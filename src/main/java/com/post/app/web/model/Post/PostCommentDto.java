package com.post.app.web.model.Post;

import java.sql.Timestamp;

public class PostCommentDto {
    private Long id;
    private String username;
    private String content;
    private Timestamp createdDate;
    private Timestamp lastModifiedDate;

    public PostCommentDto() {
    }

    public PostCommentDto(Long id, String username, String content, Timestamp createdDate, Timestamp lastModifiedDate) {
        this.id = id;
        this.username = username;
        this.content = content;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
