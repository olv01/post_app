package com.post.app.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
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
    JWTVerifier jwtVerifier;

    @Mock
    DecodedJWT decodedJWT;


    @BeforeEach
    void setUp() {
    }

    @Test
    void generateToken() {
        // By creating a static mock within a try-with-resource statement, mock will be automatically closed after test
        // If this object is never closed, the static mock will remain active on the initiating thread.
        try (MockedStatic<JWT> mocked = mockStatic(JWT.class)) {
            // Stubbing static builder
            mocked.when(JWT::create).thenReturn(builder);
            mocked.when(() -> builder.withSubject(any())).thenReturn(builder);
            mocked.when(() -> builder.withClaim(anyString(), anyString())).thenReturn(builder);
            mocked.when(() -> builder.withIssuedAt(any(Date.class))).thenReturn(builder);
            mocked.when(() -> builder.withExpiresAt(any(Date.class))).thenReturn(builder);
            mocked.when(() -> builder.withIssuer(any())).thenReturn(builder);
            mocked.when(() -> builder.sign(any())).thenReturn("generatedKey");

            JWTDto result = jwtProvideService.generateToken(authentication);

            assertThat(result.getToken()).isEqualTo("generatedKey");

            // JWTProvideService.generateToken() should call JWT.create();
            mocked.verify(JWT::create);
        }
    }

    @Test
    void retrieveUsernameFrom() {
        // By creating a static mock within a try-with-resource statement, mock will be automatically closed after test
        // If this object is never closed, the static mock will remain active on the initiating thread.
        try (MockedStatic<JWT> mocked = mockStatic(JWT.class)) {
            // Stubbing static builder
            mocked.when(() -> JWT.require(any())).thenReturn(verification);
            mocked.when(() -> verification.withIssuer(anyString())).thenReturn(verification);
            mocked.when(() -> verification.build()).thenReturn(jwtVerifier);
            mocked.when(() -> jwtVerifier.verify(anyString())).thenReturn(decodedJWT);
            mocked.when(() -> decodedJWT.getSubject()).thenReturn("John");

            Optional<String> returnedUsername = jwtProvideService.retrieveUsernameFrom("encryptedToken");

            assertThat(returnedUsername.get()).isEqualTo("John");

            // JWTProvideService.retrieveUsernameFrom() should call JWT.require() with any Algorithm
            mocked.verify(() -> JWT.require(any(Algorithm.class)));
        }
    }
}