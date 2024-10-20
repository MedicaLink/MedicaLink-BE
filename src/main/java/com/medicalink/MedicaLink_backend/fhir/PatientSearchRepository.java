package com.medicalink.MedicaLink_backend.fhir;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientSearchRepository extends CrudRepository<PatientSearchParams,String>, JpaSpecificationExecutor<PatientSearchParams> {
    PatientSearchParams findByNic(String nic);
}
