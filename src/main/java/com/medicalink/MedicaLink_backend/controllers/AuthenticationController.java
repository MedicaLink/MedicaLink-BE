package com.medicalink.MedicaLink_backend.controllers;

import com.medicalink.MedicaLink_backend.dto.*;
import com.medicalink.MedicaLink_backend.models.User;
import com.medicalink.MedicaLink_backend.services.AuthenticationService;
import com.medicalink.MedicaLink_backend.utils.ApiError;
import com.medicalink.MedicaLink_backend.utils.ApiResponse;
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

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse, ApiError>> refreshToken(@RequestBody RefreshTokenDto input) {
        LoginResponse response = authenticationService.refreshToken(input.getRefreshToken());
        return ResponseEntity.ok(new ApiResponse<>(response, null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse, ApiError>> authenticate(@RequestBody LoginUserDto input) {
        LoginResponse loginResponse = authenticationService.authenticate(input);
        return ResponseEntity.ok(new ApiResponse<>(loginResponse, null));
    }

    @PostMapping("/practitioner/signup")
    public ResponseEntity<ApiResponse<RegisterResponse, ApiError>> registerPractitioner(@RequestBody RegisterPractitionerDto input) {
        try {
            User newUser = authenticationService.registerPractitioner(input);
            RegisterResponse registerResponse = new RegisterResponse()
                    .setStatus("Practitioner registered with id " + newUser.getId())
                    .setUserName(newUser.getUserName());

            return ResponseEntity.ok(new ApiResponse<>(registerResponse, null));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(null, new ApiError(e.getMessage())));
        }
    }

    @PostMapping("/patient/signup")
    public ResponseEntity<ApiResponse<RegisterResponse, ApiError>> registerPatient(@RequestBody RegisterUserDto input) {
        User newUser = authenticationService.registerUser(input);

        RegisterResponse registerResponse = new RegisterResponse()
                .setStatus("Account Created")
                .setUserName(newUser.getUserName());

        return ResponseEntity.ok(new ApiResponse<>(registerResponse, null));
    }
}
