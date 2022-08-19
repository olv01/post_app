package com.post.app.services;

import com.post.app.model.JWTDto;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface JWTProvideService {
    JWTDto generateToken(Authentication authentication);
    Optional<String> retrieveUsernameFrom(String encryptedToken);
}
