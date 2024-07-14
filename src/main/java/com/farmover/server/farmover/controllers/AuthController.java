package com.farmover.server.farmover.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.farmover.server.farmover.payloads.AuthenticationResponse;
import com.farmover.server.farmover.payloads.request.LoginRequest;
import com.farmover.server.farmover.payloads.request.UserRequestDto;
import com.farmover.server.farmover.services.impl.AuthServiceImpl;

@RestController
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthServiceImpl authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserRequestDto userDto) {
        AuthenticationResponse registeredUser = authService.register(userDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) {
        AuthenticationResponse authenticatedUser = authService.authenticate(request);

        return ResponseEntity.ok(authenticatedUser);
    }

}
