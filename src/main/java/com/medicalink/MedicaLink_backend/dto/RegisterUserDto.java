package com.medicalink.MedicaLink_backend.dto;

import com.medicalink.MedicaLink_backend.utils.enums.UserRoles;

import java.util.List;

public class RegisterUserDto {
    private String userName;

    private String password;

    private List<UserRoles> roles;

    public List<UserRoles> getRoles() {
        return roles;
    }

    public RegisterUserDto setRoles(List<UserRoles> roles) {
        this.roles = roles;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public RegisterUserDto setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RegisterUserDto setPassword(String password) {
        this.password = password;
        return this;
    }
}
