package com.post.app.web.services;

import com.post.app.domain.ERole;
import com.post.app.domain.Role;
import com.post.app.domain.User;
import com.post.app.model.JWTDto;
import com.post.app.repositories.RoleRepository;
import com.post.app.repositories.UserRepository;
import com.post.app.services.JWTProvideService;
import com.post.app.web.model.auth.JWTResponse;
import com.post.app.web.model.auth.SignInRequest;
import com.post.app.web.model.auth.SignUpRequest;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthenticationServiceImpl implements AuthenticationService, SmartInitializingSingleton {

    private final JWTProvideService jwtProvideService;
    private final PasswordEncoder encoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private AuthenticationManager authenticationManager;

    public AuthenticationServiceImpl(JWTProvideService jwtProvideService,
                                     PasswordEncoder encoder,
                                     AuthenticationManagerBuilder authenticationManagerBuilder,
                                     UserRepository userRepository,
                                     RoleRepository roleRepository) {
        this.jwtProvideService = jwtProvideService;
        this.encoder = encoder;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    // Needed for testing
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // To authenticate user on its own, AuthenticationManager bean is needed.
    // It implements SmartInitializingSingleton.afterSingletonsInstantiated() to get the AuthenticationManager bean
    // after it's initialized. Otherwise, AuthenticationManager bean won't work.
    @Override
    public void afterSingletonsInstantiated() {
        this.authenticationManager = authenticationManagerBuilder.getObject();
    }

    // After it authenticates the user, it returns JWTResponse with newly created token
    @Override
    public JWTResponse authenticateUser(SignInRequest signInRequest) {
        String username = signInRequest.getUsername();
        String password = signInRequest.getPassword();

        // authenticate user by given information
        // If the user is authenticated successfully, Authentication should hold user information
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authToken);

        // generate jwt token
        JWTDto jwtDto = jwtProvideService.generateToken(authentication);
        return new JWTResponse("Login Successful",
                username,
                jwtDto.getToken(),
                jwtDto.getExpiredAt());
    }

    @Transactional
    @Override
    public JWTResponse createNewUser(SignUpRequest signUpRequest) {
        String username = signUpRequest.getUsername();
        String password = signUpRequest.getPassword();
        String email = signUpRequest.getEmail();

        // check if username already exists
        if (userRepository.existsByUsername(username)) {
            throw new BadCredentialsException("사용자가 이미 존재합니다.");
        }

        // retrieve role object
        Optional<Role> role_user = roleRepository.findByName(ERole.ROLE_USER);
        if (role_user.isEmpty()) {
            throw new AuthenticationServiceException("Role now found");
        }

        User user = new User(username, encoder.encode(password), email, Set.of(role_user.get()));
        userRepository.save(user);

        // after new user created, it returns jwt token
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, password);
        JWTDto jwtDto = jwtProvideService.generateToken(authToken);

        return new JWTResponse("New User Created",
                username,
                jwtDto.getToken(),
                jwtDto.getExpiredAt());
    }

}
