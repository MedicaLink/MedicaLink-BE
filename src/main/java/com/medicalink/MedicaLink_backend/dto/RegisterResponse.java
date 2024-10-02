package com.medicalink.MedicaLink_backend.dto;

public class RegisterResponse {
    private String status;

    private String userName;

    public String getStatus() {
        return status;
    }

    public RegisterResponse setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public RegisterResponse setUserName(String userName) {
        this.userName = userName;
        return this;
    }
}
