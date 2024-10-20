package com.medicalink.MedicaLink_backend.fhir;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class PatientSearchParams {
    @Id
    @GeneratedValue
    private Long id;
    private String patientId;
    private String nic;
    private String name;
    private String address;
    private Date bithDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public PatientSearchParams setPatientId(String patientId) {
        this.patientId = patientId;
        return this;
    }

    public String getNic() {
        return nic;
    }

    public PatientSearchParams setNic(String nic) {
        this.nic = nic;
        return this;
    }

    public String getName() {
        return name;
    }

    public PatientSearchParams setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public PatientSearchParams setAddress(String address) {
        this.address = address;
        return this;
    }

    public Date getBithDate() {
        return bithDate;
    }

    public PatientSearchParams setBithDate(Date bithDate) {
        this.bithDate = bithDate;
        return this;
    }
}
