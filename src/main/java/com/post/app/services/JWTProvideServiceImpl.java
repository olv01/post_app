package com.post.app.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.post.app.model.JWTDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JWTProvideServiceImpl implements JWTProvideService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final String secretKey;
    private final Long validityInMs;

    public JWTProvideServiceImpl(@NotNull @Value("${jwt.secret-key}") String secretKey,
                                 @Value("${jwt.validity-in-ms}") Long validityInMs) {
        this.secretKey = secretKey;
        this.validityInMs = validityInMs;
    }

    @Override
    public JWTDto generateToken(Authentication authentication) {
        // generate token based on the authentication information
        String username = authentication.getName();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Date currentDate = new Date();
        Date expiredAt = new Date(currentDate.getTime() + validityInMs);

        String generatedToken = JWT.create()
                .withSubject(username)
                .withClaim("roles", authorities)
                .withIssuedAt(currentDate)
                .withExpiresAt(expiredAt)
                .withIssuer("post-app")
                .sign(Algorithm.HMAC256(secretKey));
        return new JWTDto(generatedToken, expiredAt);
    }

    @Override
    public Optional<String> retrieveUsernameFrom(String encryptedToken) {
        // if token is verified, return username
        try {
            String username = JWT.require(Algorithm.HMAC256(secretKey))
                    .withIssuer("post-app")
                    .build()
                    .verify(encryptedToken)
                    .getSubject();
            return Optional.of(username);
        } catch (JWTVerificationException | IllegalArgumentException e) {
            log.debug("Invalid JWT: {}", e.getMessage());
        }
        return Optional.empty();
    }
}
