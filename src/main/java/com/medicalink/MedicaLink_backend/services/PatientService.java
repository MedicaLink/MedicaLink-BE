package com.medicalink.MedicaLink_backend.services;
import com.medicalink.MedicaLink_backend.dto.RegisterPatientDto;
import com.medicalink.MedicaLink_backend.dto.SearchPatientDto;
import com.medicalink.MedicaLink_backend.fhir.FhirManager;
import com.medicalink.MedicaLink_backend.fhir.PatientSearchParams;
import com.medicalink.MedicaLink_backend.fhir.PatientSearchRepository;
import com.medicalink.MedicaLink_backend.fhir.PatientSearchSpecification;
import org.hl7.fhir.r4.model.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PatientService {
    private FhirManager fhirManager;
    private PatientSearchRepository pSearchRepo;

    public PatientService(FhirManager fhirManager, PatientSearchRepository pSearchRepo) {
        this.fhirManager = fhirManager;
        this.pSearchRepo = pSearchRepo;
    }

    public Bundle getPatients() {
        return fhirManager.getClient().search()
                .forResource(Patient.class)
                .returnBundle(Bundle.class)
                .encodedJson()
                .execute();
    }

    public Bundle searchPatients(SearchPatientDto input) {
        var filter = input.getFilters();
        String name = filter.getName() == null? "" : filter.getName().toLowerCase();
        String nic = filter.getNic() == null? "" : filter.getNic().toLowerCase();
        String address = filter.getAddress() == null? "" : filter.getAddress().toLowerCase();

        System.out.println("Params: " + name + " " + nic + " " + address);
        Specification<PatientSearchParams> query = PatientSearchSpecification
                .withName(name)
                .and(PatientSearchSpecification.withNic(nic))
                .and(PatientSearchSpecification.withAddress(address));

        Stream<PatientSearchParams> params = pSearchRepo
                .findAll(query)
                .stream();
        if (input.getSortField() != null) { // Sort the array
            params = params.sorted((prev,next) -> {
                int prevResult = 0, nextResult = 0;
                String sortField = input.getSortField();

                if (sortField.equalsIgnoreCase("name")) {
                    prevResult = prev.getName().toLowerCase().indexOf(name);
                    nextResult = next.getName().toLowerCase().indexOf(name);
                } else if (sortField.equalsIgnoreCase("nic")) {
                    prevResult = prev.getNic().indexOf(nic);
                    nextResult = next.getNic().indexOf(nic);
                } else if (sortField.equalsIgnoreCase("address")) {
                    prevResult = prev.getAddress().indexOf(address);
                    nextResult = next.getAddress().indexOf(address);
                }

                return Integer.compare(prevResult, nextResult);
            });
        }
        List<String> patientIds = params
                .map(PatientSearchParams::getPatientId)
                .collect(Collectors.toList());
        System.out.println("PatientIDs: " + patientIds);

        return (Bundle) fhirManager.getClient().search()
                .forResource(Patient.class)
                .where(Patient.RES_ID.exactly().codes(patientIds))
                .execute();
    }

    public boolean addPatient(RegisterPatientDto input) {
        // Check if a patient with the given nic exists
        var check = pSearchRepo.findByNic(input.getNic());
        if (check != null) {
            throw new RuntimeException("Patient already exists");
        }

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

        var result = fhirManager.getClient().create()
                .resource(patient)
                .prettyPrint()
                .encodedJson()
                .execute();

        String patientId = result.getId().getIdPart();
        System.out.println("RESOURCE ID" + patientId);

        PatientSearchParams params = new PatientSearchParams()
                .setPatientId(patientId)
                .setNic(input.getNic())
                .setName(input.getGivenName() + " " + input.getFamilyName())
                .setAddress(input.getDistrict())
                .setBithDate(input.getDateOfBirth());
        pSearchRepo.save(params);

        return result.getCreated();
    }
}
