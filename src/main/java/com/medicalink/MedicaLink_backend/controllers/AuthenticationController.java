package com.medicalink.MedicaLink_backend.controllers;

import com.medicalink.MedicaLink_backend.dto.LoginResponse;
import com.medicalink.MedicaLink_backend.dto.LoginUserDto;
import com.medicalink.MedicaLink_backend.dto.RegisterResponse;
import com.medicalink.MedicaLink_backend.dto.RegisterUserDto;
import com.medicalink.MedicaLink_backend.models.User;
import com.medicalink.MedicaLink_backend.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody RegisterUserDto input) {
        User newUser = authenticationService.registerUser(input);

        RegisterResponse registerResponse = new RegisterResponse()
                .setStatus("Account Created")
                .setUserName(newUser.getUserName());

        return ResponseEntity.ok(registerResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(@RequestBody String refreshToken) {
        LoginResponse response = authenticationService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto input) {
        LoginResponse loginResponse = authenticationService.authenticate(input);
        return ResponseEntity.ok(loginResponse);
    }
}
