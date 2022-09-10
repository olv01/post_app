package com.post.app.web.model.auth;

public class UserCheckRequest {

    //    @NotBlank
    String username;

    public UserCheckRequest() {
    }

    public UserCheckRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
