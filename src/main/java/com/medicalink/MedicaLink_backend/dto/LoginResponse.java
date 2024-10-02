package com.medicalink.MedicaLink_backend.dto;

public class LoginResponse {
    private String token;

    public String getToken() {
        return token;
    }

    public LoginResponse setToken(String token) {
        this.token = token;
        return this;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public LoginResponse setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    private Long expiresIn;
}
