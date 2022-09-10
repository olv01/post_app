package com.post.app.web.controllers;

import com.post.app.web.model.auth.*;
import com.post.app.web.services.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authService;

    public AuthController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/signIn")
    public ResponseEntity<JWTResponse> signIn(@RequestBody SignInRequest signInRequest) {
        return new ResponseEntity<>(authService.authenticateUser(signInRequest), HttpStatus.OK);
    }

    @PostMapping("/signUp")
    public ResponseEntity<JWTResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        return new ResponseEntity<>(authService.createNewUser(signUpRequest), HttpStatus.CREATED);
    }

    @PostMapping("/checkUsername")
    public ResponseEntity<UserCheckResponse> checkUsername(@RequestBody UserCheckRequest userCheckRequest) {
        return new ResponseEntity<>(authService.checkUsername(userCheckRequest), HttpStatus.OK);
    }

}
