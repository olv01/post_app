package com.post.app.security;

import com.post.app.services.JWTProvideService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class JWTAuthenticationFilterTest {

    @InjectMocks
    JWTAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    JWTProvideService jwtProvideService;

    @Mock
    UserDetailsService userDetailsService;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    FilterChain filterChain;

    @Mock
    UserDetails userDetails;

    @Mock
    SecurityContext securityContext;

    @Test
    void doFilterInternal() throws ServletException, IOException {
        given(request.getHeader(anyString())).willReturn("Bearer encryptedToken");
        given(jwtProvideService.retrieveUsernameFrom(anyString())).willReturn(Optional.of("John"));
        given(userDetailsService.loadUserByUsername(anyString())).willReturn(userDetails);

        // mock static method SecurityContextHolder.getContext()
        try (MockedStatic<SecurityContextHolder> mocked = mockStatic(SecurityContextHolder.class)) {
            mocked.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
            then(securityContext).should().setAuthentication(any(Authentication.class));
        }

        then(request).should().getHeader(anyString());
        then(jwtProvideService).should().retrieveUsernameFrom(anyString());
        then(userDetailsService).should().loadUserByUsername(anyString());
    }
}