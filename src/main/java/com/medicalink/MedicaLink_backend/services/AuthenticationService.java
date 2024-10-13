package com.medicalink.MedicaLink_backend.services;

import com.medicalink.MedicaLink_backend.config.SessionData;
import com.medicalink.MedicaLink_backend.dto.*;
import com.medicalink.MedicaLink_backend.fhir.FhirManager;
import com.medicalink.MedicaLink_backend.models.AppUserDetails;
import com.medicalink.MedicaLink_backend.models.User;
import com.medicalink.MedicaLink_backend.models.UserSession;
import com.medicalink.MedicaLink_backend.repositories.UserRepository;
import com.medicalink.MedicaLink_backend.repositories.UserSessionRepository;
import com.medicalink.MedicaLink_backend.utils.HttpExceptions.NotFoundException;
import com.medicalink.MedicaLink_backend.utils.HttpExceptions.UnAuthorizedException;
import com.medicalink.MedicaLink_backend.utils.enums.UserRoles;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Practitioner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final UserSessionRepository sessionRepository;

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final FhirManager fhirManager;

    public AuthenticationService(
            UserRepository userRepository,
            UserSessionRepository sessionRepository,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            FhirManager fhirManager
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
        this.fhirManager = fhirManager;
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

    /**
     * Registers a new practitioner
     * @param input FE Payload
     */
    public User registerPractitioner(RegisterPractitionerDto input) throws Exception {
        User user;
        if(input.getUserId() == null) {
            // If the doesn't exist create the user
            user = new User(
                    input.getUserName(),
                    passwordEncoder.encode(input.getPassword()),
                    new ArrayList<>()
            );
        } else {
            user = userRepository.findById(input.getUserId())
                    .orElseThrow(() -> new NotFoundException("User Not Found, wrong Id"));
        }

        var userRoles = user.getRoles();
        if(userRoles.contains(UserRoles.PRACTITIONER)) {
            throw new Exception("This practitioner already exists");
        }
        userRoles.add(UserRoles.PRACTITIONER);

        user = userRepository.save(user);
        if(user.getId() == null) {
            throw new Exception("Could not create account");
        }

        // Create the practitioner resource
        List<Identifier> identifiers = List.of(
                new Identifier().setSystem("MedicaLink").setValue(input.getNic()),
                new Identifier().setSystem("MedicaLink").setValue(user.getId().toString()));
        HumanName name = new HumanName().addGiven(input.getGivenName())
                .setFamily(input.getFamilyName());
        Practitioner practitioner = new Practitioner()
                .setActive(true).setIdentifier(identifiers)
                .setName(List.of(name));

        fhirManager.getClient().create()
                .resource(practitioner)
                .encodedJson()
                .prettyPrint()
                .execute();
        return user;
    }

    /**
     * Generates a unique UUID for the session
     */
    public UUID getNextSessionId() {
        return UUID.randomUUID();
    }

    /**
     * Saves a login record including session information
     * @param loginRecord the login record
     */
    public void saveLoginRecord(UserSession loginRecord) {
        sessionRepository.save(loginRecord);
    }

    /**
     * Generates a new Token using the provided refresh token
     * @param refreshToken the refresh token
     */
    public LoginResponse refreshToken(String refreshToken) {
        UUID sessionId;
        try {
            sessionId = UUID.fromString(SessionData.getSessionId());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Malformed session data");
        }

        UserSession sessionRecord = sessionRepository.findById(sessionId)
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
        String jwtToken = jwtService.generateToken(new AppUserDetails(userDetails), sessionRecord.getId());
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
                .orElseThrow(
                        () -> new NotFoundException("User doesn't exist")
                );

        UUID sessionId = getNextSessionId();
        String jwtToken = jwtService.generateToken(new AppUserDetails(authenticatedUser), sessionId);
        String refreshToken = jwtService.generateRefreshToken(sessionId, authenticatedUser.getId());

        saveLoginRecord(
                new UserSession(
                        authenticatedUser.getId(),
                        refreshToken,
                        jwtService.getTokenExpirationDate(jwtService.getRefreshTokenExpirationTime()),
                        false
                ).setId(sessionId)
        );
        System.out.println("GOING TO authenticate");
        return new LoginResponse().setToken(jwtToken)
                .setRefreshToken(refreshToken)
                .setExpiresIn(jwtService.getExpirationTime());
    }

    public BaseResponse logOut() {
        UUID sessionId;
        try {
            sessionId = UUID.fromString(SessionData.getSessionId());
        } catch (Exception e) {
            throw new RuntimeException("Malformed session data");
        }

        sessionRepository.deleteById(sessionId);
        return new BaseResponse("Logut successfull", true);
    }
}
