package com.medicalink.MedicaLink_backend.dto;

import java.util.List;

public class RegisterUserDto {
    private String userName;

    private String password;

    public List<String> getRoles() {
        return roles;
    }

    public RegisterUserDto setRoles(List<String> roles) {
        this.roles = roles;
        return this;
    }

    private List<String> roles;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
