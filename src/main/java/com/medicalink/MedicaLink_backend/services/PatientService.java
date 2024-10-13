package com.medicalink.MedicaLink_backend.services;
import com.medicalink.MedicaLink_backend.fhir.FhirManager;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class PatientService {
    private FhirManager fhirManager;

    public PatientService(FhirManager fhirManager) {
        this.fhirManager = fhirManager;
    }

    public Bundle getPatients() {
        return fhirManager.getClient().search()
                .forResource(Patient.class)
                .returnBundle(Bundle.class)
                .encodedJson()
                .execute();
    }

    public Bundle searchPatients(String name) {
        Bundle bundle = fhirManager.getClient().search()
                .forResource(Patient.class)
                .where(Patient.GIVEN.contains().value(name))
                .returnBundle(Bundle.class)
                .encodedJson()
                .execute();

        var entries = bundle.getEntry().stream()
                .map(entry -> (Patient) entry.getResource())
                .sorted((resource1, resource2) -> {
                    int i1 = resource1.getName().get(0).getGiven().get(0).toString().indexOf(name);
                    int i2 = resource2.getName().get(0).getGiven().get(0).toString().indexOf(name);

                    return i1 - i2;
                })
                .map(patient -> new Bundle.BundleEntryComponent()
                        .setResource(patient))
                .collect(Collectors.toList());

        return bundle.setEntry(entries);
    }

    public boolean addPatient(Patient patient) {
        var result = fhirManager.getClient().create()
                .resource(patient)
                .prettyPrint()
                .encodedJson()
                .execute();

        return result.getCreated();
    }
}
