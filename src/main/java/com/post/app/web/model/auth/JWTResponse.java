package com.post.app.web.model.auth;

import com.post.app.web.model.BaseResponse;

import java.util.Date;

public class JWTResponse extends BaseResponse {
    private String username;
    private String token;
    private Date expiredAt;

    public JWTResponse(String message, String username, String token, Date expiredAt) {
        super(message);
        this.username = username;
        this.token = token;
        this.expiredAt = expiredAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Date expiredAt) {
        this.expiredAt = expiredAt;
    }

    @Override
    public Date getTimestamp() {
        return super.getTimestamp();
    }

    @Override
    public void setTimestamp(Date timestamp) {
        super.setTimestamp(timestamp);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public void setMessage(String message) {
        super.setMessage(message);
    }
}
