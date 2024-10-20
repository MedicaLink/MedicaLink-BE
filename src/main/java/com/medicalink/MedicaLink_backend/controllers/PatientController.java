package com.medicalink.MedicaLink_backend.controllers;

import com.medicalink.MedicaLink_backend.dto.BaseResponse;
import com.medicalink.MedicaLink_backend.dto.RegisterPatientDto;
import com.medicalink.MedicaLink_backend.dto.SearchPatientDto;
import com.medicalink.MedicaLink_backend.fhir.FhirManager;
import com.medicalink.MedicaLink_backend.services.PatientService;
import com.medicalink.MedicaLink_backend.utils.ApiError;
import com.medicalink.MedicaLink_backend.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/patient")
@RestController
public class PatientController {
    private final PatientService patientService;
    private final FhirManager fhirManager;

    public PatientController(PatientService patientService, FhirManager fhirManager) {
        this.patientService = patientService;
        this.fhirManager = fhirManager;
    }

    @GetMapping(produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> getPatients() {
        return ResponseEntity.ok(
                fhirManager.getContext().newJsonParser()
                        .setPrettyPrint(true)
                        .encodeResourceToString(patientService.getPatients())
        );
    }

    @PostMapping(path = "/search",produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> searchPatients(@RequestBody SearchPatientDto input) {

        return ResponseEntity.ok(
                fhirManager.getContext()
                        .newJsonParser()
                        .setPrettyPrint(true)
                        .encodeResourceToString(patientService.searchPatients(input))
        );
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<BaseResponse, ApiError>> registerPatient(@RequestBody RegisterPatientDto input) {
        boolean patientSaved = patientService.addPatient(input);

        BaseResponse response;
        if (patientSaved) {
            response = new BaseResponse()
                    .setMessage("Patient registered successfully")
                    .setSuccess(true);
        } else {
            response = new BaseResponse()
                    .setMessage("Could not save patient")
                    .setSuccess(false);
        }
        return ResponseEntity
                .ok(new ApiResponse<>(response, null));
    }
}
