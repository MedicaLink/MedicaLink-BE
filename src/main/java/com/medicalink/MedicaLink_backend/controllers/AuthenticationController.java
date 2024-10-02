package com.medicalink.MedicaLink_backend.controllers;

import com.medicalink.MedicaLink_backend.dto.LoginResponse;
import com.medicalink.MedicaLink_backend.dto.LoginUserDto;
import com.medicalink.MedicaLink_backend.dto.RegisterResponse;
import com.medicalink.MedicaLink_backend.dto.RegisterUserDto;
import com.medicalink.MedicaLink_backend.models.AppUserDetails;
import com.medicalink.MedicaLink_backend.models.User;
import com.medicalink.MedicaLink_backend.services.AuthenticationService;
import com.medicalink.MedicaLink_backend.services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto input) {
        User authenticatedUser = authenticationService.authenticate(input);

        String jwtToken = jwtService.generateToken(new AppUserDetails(authenticatedUser));

        LoginResponse loginResponse = new LoginResponse().setToken(jwtToken)
                .setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}
