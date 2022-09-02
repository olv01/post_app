package com.post.app.web.model;

public class ErrorResponse extends BaseResponse {
    private String error;

    public ErrorResponse(String message, String error) {
        super(message);
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
