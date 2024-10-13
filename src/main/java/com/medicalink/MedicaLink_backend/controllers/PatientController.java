package com.medicalink.MedicaLink_backend.controllers;

import com.medicalink.MedicaLink_backend.dto.RegisterPatientDto;
import com.medicalink.MedicaLink_backend.fhir.FhirManager;
import com.medicalink.MedicaLink_backend.services.PatientService;
import jakarta.validation.Valid;
import org.hl7.fhir.r4.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(path = "/search",produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> searchPatients(@RequestParam String name) {
        return ResponseEntity.ok(
                fhirManager.getContext()
                        .newJsonParser()
                        .setPrettyPrint(true)
                        .encodeResourceToString(patientService.searchPatients(name))
        );
    }

    @PostMapping("/add")
    public ResponseEntity<String> registerPatient(@Valid @RequestBody RegisterPatientDto input) {
        Patient patient = new Patient()
                .setIdentifier(List.of(new Identifier().setValue(input.getNic())))
                .setName(List.of(
                        new HumanName()
                        .addGiven(input.getGivenName())
                        .setFamily(input.getFamilyName())))
                .setBirthDate(input.getDateOfBirth())
                .setAddress(
                        List.of(new Address().setDistrict(input.getDistrict()))
                )
                .setTelecom(
                        List.of(new ContactPoint().setValue(input.getTelecom()))
                );

        boolean patientSaved = patientService.addPatient(patient);
        return ResponseEntity
                .ok(patientSaved? "Patient registered successfully" : "Could not save patient");
    }
}
