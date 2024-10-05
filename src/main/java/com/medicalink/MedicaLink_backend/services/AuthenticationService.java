package com.medicalink.MedicaLink_backend.services;

import com.medicalink.MedicaLink_backend.dto.LoginResponse;
import com.medicalink.MedicaLink_backend.dto.LoginUserDto;
import com.medicalink.MedicaLink_backend.dto.RegisterUserDto;
import com.medicalink.MedicaLink_backend.models.AppUserDetails;
import com.medicalink.MedicaLink_backend.models.User;
import com.medicalink.MedicaLink_backend.models.UserSession;
import com.medicalink.MedicaLink_backend.repositories.UserRepository;
import com.medicalink.MedicaLink_backend.repositories.UserSessionRepository;
import com.medicalink.MedicaLink_backend.utils.HttpExceptions.NotFoundException;
import com.medicalink.MedicaLink_backend.utils.HttpExceptions.UnAuthorizedException;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final UserSessionRepository sessionRepository;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            UserSessionRepository sessionRepository,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a user to the application
     * @param input RegisterUserDto
     */
    public User registerUser(RegisterUserDto input) {
        User user = new User(
                input.getUserName(),
                passwordEncoder.encode(input.getPassword()),
                input.getRoles()
        );

        return userRepository.save(user);
    }

    public Long getNextSessionId() {
        return sessionRepository.findNextGeneratedId();
    }

    public void saveLoginRecord(UserSession loginRecord) {
        sessionRepository.save(loginRecord);
    }

    /**
     * Generates a new Token using the provided refresh token
     * @param refreshToken the refresh token
     */
    public LoginResponse refreshToken(String refreshToken) {
        long recordId;
        try {
            recordId = Long.parseLong(jwtService.extractClaim(refreshToken, Claims::getId));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Malformed session data");
        }

        UserSession sessionRecord = sessionRepository.findById(recordId)
                .orElseThrow(
                        () -> new NotFoundException("Session not found")
                );
        if (sessionRecord.getExpiresAt().before(new Date()) || sessionRecord.isRevoked()) {
            throw new UnAuthorizedException("Invalid refresh token");
        }

        User userDetails = userRepository
                .findById(sessionRecord.getUserId())
                .orElseThrow(() ->
                        new NotFoundException("User not found")
                );
        String jwtToken = jwtService.generateToken(new AppUserDetails(userDetails));
        refreshToken = jwtService.generateRefreshToken(sessionRecord.getId(), userDetails.getId());

        saveLoginRecord(
                sessionRecord
                        .setRefreshToken(refreshToken)
                        .setExpiresAt(jwtService.getTokenExpirationDate(jwtService.getRefreshTokenExpirationTime()))
        );

        return new LoginResponse().setToken(jwtToken)
                .setRefreshToken(refreshToken)
                .setExpiresIn(jwtService.getExpirationTime());
    }

    /**
     * Authenticates a user when logging in
     * @param input LoginUserDto
     */
    public LoginResponse authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUserName(),
                        input.getPassword()
                )
        );

        User authenticatedUser = userRepository.findByUserName(input.getUserName())
                .orElseThrow();

        String jwtToken = jwtService.generateToken(new AppUserDetails(authenticatedUser));

        Long refreshTokenId = getNextSessionId();
        String refreshToken = jwtService.generateRefreshToken(refreshTokenId, authenticatedUser.getId());

        saveLoginRecord(
                new UserSession(
                        authenticatedUser.getId(),
                        refreshToken,
                        jwtService.getTokenExpirationDate(jwtService.getRefreshTokenExpirationTime()),
                        false
                )
        );

        return new LoginResponse().setToken(jwtToken)
                .setRefreshToken(refreshToken)
                .setExpiresIn(jwtService.getExpirationTime());
    }
}
