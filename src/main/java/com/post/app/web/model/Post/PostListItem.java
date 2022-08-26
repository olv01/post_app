package com.post.app.web.model.Post;

import java.sql.Timestamp;
import java.util.Date;

public class PostListItem {
    private Long id;
    private String title;
    private String username;
    private Timestamp createdDate;
    private Timestamp lastModifiedDate;
    private Long commentCount;

    public PostListItem() {
    }

    public PostListItem(Long id, String title, String username, Date createdDate, Date lastModifiedDate, Long commentCount) {
        this.id = id;
        this.title = title;
        this.username = username;
        this.createdDate = (Timestamp) createdDate;
        this.lastModifiedDate = (Timestamp) lastModifiedDate;
        this.commentCount = commentCount;
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

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }
}
