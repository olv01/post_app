package com.post.app.web.services;

import com.post.app.web.model.auth.JWTResponse;
import com.post.app.web.model.auth.SignInRequest;
import com.post.app.web.model.auth.SignUpRequest;

public interface AuthenticationService {
    JWTResponse authenticateUser(SignInRequest signInRequest);

    JWTResponse createNewUser(SignUpRequest signUpRequest);
}
