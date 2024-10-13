package com.medicalink.MedicaLink_backend.controllers;

import com.medicalink.MedicaLink_backend.dto.BaseResponse;
import com.medicalink.MedicaLink_backend.services.AuthenticationService;
import com.medicalink.MedicaLink_backend.utils.ApiError;
import com.medicalink.MedicaLink_backend.utils.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/me")
@RestController
public class TestController {
    private final AuthenticationService authenticationService;

    public TestController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/test")
    public ResponseEntity<String> testGet() {
        return ResponseEntity.ok("Able to access /test");
    }

    @PreAuthorize("hasRole('PRACTITIONER')")
    @GetMapping("/practitioner")
    public ResponseEntity<String> testGetPractitioner() {
        return ResponseEntity.ok("Able to access /practitioner");
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/patient")
    public ResponseEntity<String> testGetPatient() {
        return ResponseEntity.ok("Able to access /patient");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<String> testGetAdmin() {
        return ResponseEntity.ok("Able to access /admin");
    }

    @PostMapping("/test")
    public ResponseEntity<String> testPost() {
        return ResponseEntity.ok("Able to access /test");
    }

    @PreAuthorize("hasRole('PRACTITIONER')")
    @PostMapping("/practitioner")
    public ResponseEntity<String> testPostPractitioner() {
        return ResponseEntity.ok("Able to access /practitioner");
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping("/patient")
    public ResponseEntity<String> testPostPatient() {
        return ResponseEntity.ok("Able to access /patient");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<String> testPostAdmin() {
        return ResponseEntity.ok("Able to access /admin");
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<BaseResponse, ApiError>> logout() {
        var response = authenticationService.logOut();
        return ResponseEntity.ok(new ApiResponse<>(response, null));
    }
}
