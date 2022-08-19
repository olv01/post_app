package com.post.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests()
                    .antMatchers("/", "/**").access("permitAll()")

                // Allow access to H2 console
                .and()
                    .csrf()
                    .ignoringAntMatchers("/h2-console/**", "/api/**")
                .and()
                    .headers()
                    .frameOptions()
                    .sameOrigin()


                .and()
                    // Apply CustomUserDetailsService
                    .userDetailsService(userDetailsService)
                    // We use JWT authentication, so we don't need session management
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .build();
    }
}
