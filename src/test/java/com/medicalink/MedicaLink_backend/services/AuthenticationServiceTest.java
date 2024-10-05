package com.medicalink.MedicaLink_backend.services;

import com.medicalink.MedicaLink_backend.dto.RegisterUserDto;
import com.medicalink.MedicaLink_backend.models.User;
import com.medicalink.MedicaLink_backend.repositories.UserRepository;
import com.medicalink.MedicaLink_backend.repositories.UserSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AuthenticationServiceTest {
    private UserRepository userRepository;
    private UserSessionRepository sessionRepository;

    private JwtService jwtService;

    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    private AuthenticationService authenticationService;

    @BeforeEach
    public void beforeTest() {
        userRepository = Mockito.mock(UserRepository.class);
        sessionRepository = Mockito.mock(UserSessionRepository.class);
        jwtService = Mockito.mock(JwtService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationManager = Mockito.mock(AuthenticationManager.class);

        authenticationService = new AuthenticationService(
                userRepository,
                sessionRepository,
                authenticationManager,
                jwtService,
                passwordEncoder
        );
    }

    @Test
    @DisplayName("Should return correct response with valid inputs")
    public void registerUser_withValidInputs() {
        final RegisterUserDto mockInput = new RegisterUserDto()
                .setUserName("userName")
                .setPassword("password")
                .setRoles(List.of("user"));
        final User mockUser = new User(
                mockInput.getUserName(),
                mockInput.getPassword(),
                mockInput.getRoles()
        );
        when(userRepository.save(mockUser))
                .thenReturn(mockUser);

        assertEquals(mockUser, authenticationService.registerUser(mockInput));
    }
}