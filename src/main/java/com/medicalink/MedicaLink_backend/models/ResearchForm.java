package com.medicalink.MedicaLink_backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
public class ResearchForm {
    @Id
    @GeneratedValue
    private Long id;
    private String electronicConsent;
    private String filledBy;
    private String ethnicity;
    private String education;
    private String occupation;
    private String finances;

    private String patientId;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    public Long getId() {
        return id;
    }

    public ResearchForm setId(Long id) {
        this.id = id;
        return this;
    }

    public String getElectronicConsent() {
        return electronicConsent;
    }

    public ResearchForm setElectronicConsent(String electronicConsent) {
        this.electronicConsent = electronicConsent;
        return this;
    }

    public String getFilledBy() {
        return filledBy;
    }

    public ResearchForm setFilledBy(String filledBy) {
        this.filledBy = filledBy;
        return this;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public ResearchForm setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
        return this;
    }

    public String getEducation() {
        return education;
    }

    public ResearchForm setEducation(String education) {
        this.education = education;
        return this;
    }

    public String getOccupation() {
        return occupation;
    }

    public ResearchForm setOccupation(String occupation) {
        this.occupation = occupation;
        return this;
    }

    public String getFinances() {
        return finances;
    }

    public ResearchForm setFinances(String finances) {
        this.finances = finances;
        return this;
    }

    public String getPatientId() {
        return patientId;
    }

    public ResearchForm setPatientId(String patientId) {
        this.patientId = patientId;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public ResearchForm setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public ResearchForm setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
