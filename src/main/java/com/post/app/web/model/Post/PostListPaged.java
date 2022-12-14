package com.post.app.web.model.Post;

import org.springframework.data.domain.Pageable;

import java.util.List;

public class PostListPaged {
    private List<PostListItem> content;
    private Pageable pageable;
    private int total;
    // for searched result
    private long totalElements;

    public PostListPaged() {
    }

    public PostListPaged(List<PostListItem> content, Pageable pageable, int total) {
        this.content = content;
        this.pageable = pageable;
        this.total = total;
    }

    public PostListPaged(List<PostListItem> postList, Pageable pageable, int total, long totalElements) {
        this.content = postList;
        this.pageable = pageable;
        this.total = total;
        this.totalElements = totalElements;
    }

    public List<PostListItem> getContent() {
        return content;
    }

    public void setContent(List<PostListItem> content) {
        this.content = content;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
}
