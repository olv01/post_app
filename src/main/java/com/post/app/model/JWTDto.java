package com.post.app.model;

import java.util.Date;

public class JWTDto {
    private String token;
    private Date expiredAt;

    public JWTDto(String token, Date expiredAt) {
        this.token = token;
        this.expiredAt = expiredAt;
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
}
