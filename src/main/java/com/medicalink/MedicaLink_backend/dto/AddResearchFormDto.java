package com.medicalink.MedicaLink_backend.dto;

import java.util.Date;

public class AddResearchFormDto {
    private String electronicConsent;
    private String filledBy;
    private String bhtNumber;
    private String wardNo;
    private String consultantName;
    private String ethnicity;
    private String education;
    private String occupation;
    private String finances;
    private String patientId;

    public String getElectronicConsent() {
        return electronicConsent;
    }

    public void setElectronicConsent(String electronicConsent) {
        this.electronicConsent = electronicConsent;
    }

    public String getFilledBy() {
        return filledBy;
    }

    public void setFilledBy(String filledBy) {
        this.filledBy = filledBy;
    }

    public String getBhtNumber() {
        return bhtNumber;
    }

    public void setBhtNumber(String bhtNumber) {
        this.bhtNumber = bhtNumber;
    }

    public String getWardNo() {
        return wardNo;
    }

    public void setWardNo(String wardNo) {
        this.wardNo = wardNo;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getFinances() {
        return finances;
    }

    public void setFinances(String finances) {
        this.finances = finances;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}
