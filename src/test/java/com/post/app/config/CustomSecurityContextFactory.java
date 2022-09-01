package com.post.app.config;

import com.post.app.domain.ERole;
import com.post.app.domain.Role;
import com.post.app.domain.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Set;

public class CustomSecurityContextFactory implements WithSecurityContextFactory<WithCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithCustomUser withCustomUser) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        var authToken = new UsernamePasswordAuthenticationToken(
                new User(withCustomUser.username(),
                        "12345",
                        "John@email.com",
                        Set.of(new Role(ERole.ROLE_USER))),
                null,
                null);
        securityContext.setAuthentication(authToken);
        return securityContext;
    }
}
