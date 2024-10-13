package com.medicalink.MedicaLink_backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import org.hl7.fhir.r4.model.Enumerations;

import java.util.Date;

public class RegisterPatientDto {
    private String nic;
    private String givenName;
    private String familyName;
    private Enumerations.AdministrativeGender gender;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    @Pattern(regexp = "^+94\\d{9}", message = "Invalid contact number format. Expected format - ^+94\\d{9}")
    private String telecom;
    private String district;

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTelecom() {
        return telecom;
    }

    public void setTelecom(@Pattern(regexp = "^+94\\d{9}", message = "Invalid contact number format. Expected format - ^+94\\d{9}") String telecom) {
        this.telecom = telecom;
    }

    public Enumerations.AdministrativeGender getGender() {
        return gender;
    }

    public void setGender(Enumerations.AdministrativeGender gender) {
        this.gender = gender;
    }
}
