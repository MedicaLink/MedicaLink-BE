package com.medicalink.MedicaLink_backend.dto;

import java.util.UUID;

public class RegisterPractitionerDto extends RegisterUserDto {
    private UUID userId;
    private String givenName;
    private String familyName;
    private String nic;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }
}
