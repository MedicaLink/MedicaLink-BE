package com.medicalink.MedicaLink_backend.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.UUID;

@Entity
public class UserSession {
    @Id
    private UUID id;
    private UUID userId;

    @Lob
    private String refreshToken;
    private Date expiresAt;
    private boolean revoked = false;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    public UserSession() {}

    public UserSession(UUID userId, String refreshToken, Date expiresAt, boolean revoked) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
        this.revoked = revoked;
    }

    public UUID getId() {
        return id;
    }

    public UserSession setId(UUID id) {
        this.id = id;
        return this;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public UserSession setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public UserSession setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
