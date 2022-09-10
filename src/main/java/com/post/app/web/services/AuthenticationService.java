package com.post.app.web.services;

import com.post.app.web.model.auth.*;

public interface AuthenticationService {
    JWTResponse authenticateUser(SignInRequest signInRequest);

    JWTResponse createNewUser(SignUpRequest signUpRequest);

    UserCheckResponse checkUsername(UserCheckRequest userCheckRequest);
}
