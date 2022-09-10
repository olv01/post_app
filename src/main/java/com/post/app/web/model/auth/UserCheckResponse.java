package com.post.app.web.model.auth;

import com.post.app.web.model.BaseResponse;

public class UserCheckResponse extends BaseResponse {

    Boolean exist;

    public UserCheckResponse(String message, Boolean exist) {
        super(message);
        this.exist = exist;
    }

    public Boolean getExist() {
        return exist;
    }

    public void setExist(Boolean exist) {
        this.exist = exist;
    }
}
