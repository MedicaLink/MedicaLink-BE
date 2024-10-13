package com.medicalink.MedicaLink_backend.repositories;

import com.medicalink.MedicaLink_backend.models.ResearchForm;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ResearchRepository extends CrudRepository<ResearchForm, Long> {
    Optional<ResearchForm> findByPatientId(String patientId);
}
