package com.medicalink.MedicaLink_backend.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("#{${security.jwt.expiration-time} * 60 * 60 * 1000}")
    private long jwtExpiration;

    @Value("#{${security.jwt.refresh-expiration-time} * 60 * 60 * 1000}")
    private long refreshJwtExpiration;

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates a token with default claims
     * @param userDetails Spring security UserDetails object
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a token with extra claims
     * @param extraClaims the extra claims
     * @param userDetails Spring security UserDetails object
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration, false);
    }

    /**
     * Generates a token with the default claims
     * @param userId id of the user
     */
    public String generateRefreshToken(UUID tokenId, UUID userId) {
        return generateRefreshToken(new HashMap<>(), tokenId, userId);
    }

    /**
     * Generates a refresh token with extra claims
     * @param extraClaims the extra claims
     * @param userId id of the user
     */
    public String generateRefreshToken(Map<String, Object> extraClaims, UUID tokenId, UUID userId) {
        extraClaims.put("userId", userId);
        extraClaims.put("recordId", tokenId);
        return buildToken(extraClaims, null, refreshJwtExpiration, true);
    }

    /**
     * Creates a new JWT token
     * @param extraClaims the roles and other claims to be included in the token
     * @param userDetails Spring security UserDetails object
     * @param expiration the expiration time limit of the token
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration,
            boolean isRefreshToken
    ) {
        var tokenBuilder = Jwts
                .builder().claims(extraClaims);
        if(isRefreshToken) {
            tokenBuilder
                    .id(extraClaims.get("recordId").toString());
        } else {
            tokenBuilder
                    .subject(userDetails.getUsername())
                    .claim("roles", userDetails.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()));
        }

        return tokenBuilder
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(getTokenExpirationDate(expiration))
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * Validates the token owner and expiration
     * @param token the JWT token
     * @param userDetails Spring security UserDetails object
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Get the expiration time limit of the token
     */
    public long getExpirationTime() {
        return jwtExpiration;
    }

    /**
     * get the expiration duration of the refresh token
     */
    public long getRefreshTokenExpirationTime() {
        return refreshJwtExpiration;
    }

    /**
     * get teh expiration date calculated from current time
     * @param expirationTime the token duration
     */
    public Date getTokenExpirationDate(long expirationTime) {
        return new Date(System.currentTimeMillis() + expirationTime);
    }

    /**
     * Checks whether the token has expired
     * @param token the JWT token
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extract the username from the token
     * @param token the JWT token
     */
    public String extractUsername(String token) {
        return extractClaim(token,Claims::getSubject);
    }

    /**
     * Extract the expiration date from the token
     * @param token the JWT token
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extract user roles and other claims
     * @param token the JWT token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.
                parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Generates a sign key for the token
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
