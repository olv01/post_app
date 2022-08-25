package com.post.app.security;

import com.post.app.services.JWTProvideService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    private final JWTProvideService jwtProvideService;

    private final UserDetailsService userDetailsService;

    public JWTAuthenticationFilter(JWTProvideService jwtProvideService, UserDetailsService userDetailsService) {
        this.jwtProvideService = jwtProvideService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Resolve a token from Authorization header
        Optional<String> bearerToken = resolveToken(request);
        if (bearerToken.isPresent()) {
            // If a token is provided, verify and retrieve username from it
            Optional<String> username = jwtProvideService.retrieveUsernameFrom(bearerToken.get());
            if (username.isPresent()) {
                // Load UserDetails object from CustomUserDetailsService which delegates its job to UserRepository
                UserDetails userDetails = userDetailsService.loadUserByUsername(username.get());
                Authentication authToken = new UsernamePasswordAuthenticationToken(userDetails,
                        null,
                        userDetails.getAuthorities());

                // Set authentication principal to current SecurityContext. Now, the request is authenticated
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    // Resolve Bearer token from a request header
    private Optional<String> resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return Optional.of(bearerToken.substring(7));
        }
        return Optional.empty();
    }
}
