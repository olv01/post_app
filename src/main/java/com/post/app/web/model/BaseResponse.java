package com.post.app.web.model;

import java.util.Date;

public class BaseResponse {
    private Date timestamp = new Date();
    private String message;

    public BaseResponse(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
