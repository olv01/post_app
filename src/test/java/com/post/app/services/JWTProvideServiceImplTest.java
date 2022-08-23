package com.post.app.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.post.app.model.JWTDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JWTProvideServiceImplTest {

    @InjectMocks
    JWTProvideService jwtProvideService = new JWTProvideServiceImpl("secret", 1L);

    @Mock
    Authentication authentication;

    @Mock
    JWTCreator.Builder builder;

    @Mock
    Algorithm algorithm;

    @Mock
    Verification verification;

    @Mock
    DecodedJWT decodedJWT;


    @BeforeEach
    void setUp() {
    }

    @Test
    void generateToken() {
        // mock static method on auth0's JWT class
        try (MockedStatic<JWT> mocked = mockStatic(JWT.class)) {
            mocked.when(JWT::create).thenReturn(builder);
            mocked.when(() -> builder.withSubject(any())).thenReturn(builder);
            mocked.when(() -> builder.withClaim(anyString(), anyString())).thenReturn(builder);
            mocked.when(() -> builder.withIssuedAt(any(Date.class))).thenReturn(builder);
            mocked.when(() -> builder.withExpiresAt(any(Date.class))).thenReturn(builder);
            mocked.when(() -> builder.withIssuer(any())).thenReturn(builder);
            mocked.when(() -> builder.sign(any())).thenReturn("generatedKey");

            JWTDto result = jwtProvideService.generateToken(authentication);

            assertThat(result.getToken()).isEqualTo("generatedKey");
            mocked.verify(JWT::create);
        }
    }

    @Test
    void retrieveUsernameFrom() {
        try (MockedStatic<JWT> mocked = mockStatic(JWT.class)) {
            mocked.when(() -> JWT.require(any())).thenReturn(verification);
            mocked.when(() -> verification.withIssuer(anyString())).thenReturn(verification);
            mocked.when(() -> verification.build().verify(anyString())).thenReturn(decodedJWT);
            when(decodedJWT.getSubject()).thenReturn("John");

            Optional<String> returnedUsername = jwtProvideService.retrieveUsernameFrom("encryptedToken");

            assertThat(returnedUsername.get()).isEqualTo("John");
            mocked.verify(() -> JWT.require(algorithm));
        }
    }
}