package com.post.app.web.services;

import com.post.app.domain.ERole;
import com.post.app.domain.Role;
import com.post.app.model.JWTDto;
import com.post.app.repositories.RoleRepository;
import com.post.app.repositories.UserRepository;
import com.post.app.services.JWTProvideService;
import com.post.app.web.model.auth.JWTResponse;
import com.post.app.web.model.auth.SignInRequest;
import com.post.app.web.model.auth.SignUpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @InjectMocks
    AuthenticationServiceImpl authService;

    @Mock
    JWTProvideService jwtProvideService;

    @Mock
    PasswordEncoder encoder;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    UsernamePasswordAuthenticationToken authToken;
    JWTDto jwtDto;

    @BeforeEach
    void init() {
        authService.setAuthenticationManager(authenticationManager);

        authToken = new UsernamePasswordAuthenticationToken("John", "12345");
        jwtDto = new JWTDto("encryptedToken", new Date());
    }

    @Test
    void authenticateUser() {
        given(authenticationManager.authenticate(any())).willReturn(authToken);
        given(jwtProvideService.generateToken(authToken)).willReturn(jwtDto);

        SignInRequest signInRequest = new SignInRequest("John", "12345");
        JWTResponse result = authService.authenticateUser(signInRequest);

        assertThat(result.getToken()).isEqualTo("encryptedToken");
        assertThat(result.getUsername()).isEqualTo("John");
        assertThat(result.getMessage()).isEqualTo("Login Successful");
        then(authenticationManager).should().authenticate(any(UsernamePasswordAuthenticationToken.class));
        then(jwtProvideService).should().generateToken(any(Authentication.class));
    }

    @Test
    void createNewUser() {
        given(userRepository.existsByUsername(anyString())).willReturn(false);
        given(roleRepository.findByName(any())).willReturn(Optional.of(new Role(ERole.ROLE_USER)));
        given(encoder.encode(any())).willReturn("encryptedPassword");
        given(jwtProvideService.generateToken(any())).willReturn(jwtDto);

        SignUpRequest signUpRequest = new SignUpRequest("John", "12345", "a@b.com");
        JWTResponse result = authService.createNewUser(signUpRequest);

        assertThat(result.getToken()).isEqualTo("encryptedToken");
        assertThat(result.getUsername()).isEqualTo("John");
        assertThat(result.getMessage()).isEqualTo("New User Created");
        then(userRepository).should().existsByUsername(anyString());
        then(encoder).should().encode(anyString());
        then(jwtProvideService).should().generateToken(any(Authentication.class));
    }
}