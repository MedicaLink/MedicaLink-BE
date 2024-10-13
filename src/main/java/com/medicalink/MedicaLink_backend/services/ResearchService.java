package com.medicalink.MedicaLink_backend.services;

import com.medicalink.MedicaLink_backend.dto.AddResearchFormDto;
import com.medicalink.MedicaLink_backend.fhir.FhirManager;
import com.medicalink.MedicaLink_backend.models.ResearchForm;
import com.medicalink.MedicaLink_backend.repositories.ResearchRepository;
import org.hl7.fhir.r4.model.*;

import java.util.List;

public class ResearchService {

    private final ResearchRepository researchRepo;

    private final FhirManager fhirManager;

    public ResearchService(ResearchRepository researchRepo, FhirManager fhirManager) {
        this.researchRepo = researchRepo;
        this.fhirManager = fhirManager;
    }

    public ResearchForm addResearchForm(AddResearchFormDto data) {
        // Query the practitioner using the userId
        Encounter encounter = new Encounter()
                .setClass_(new Coding("MedicaLink","inpatient","inpatient"))
                .setStatus(Encounter.EncounterStatus.FINISHED)
                .setIdentifier(
                        List.of(new Identifier().setValue(data.getBhtNumber()))
                );

        CareTeam.CareTeamParticipantComponent participant= new CareTeam.CareTeamParticipantComponent()
                .setRole(
                        List.of(new CodeableConcept()
                                .setText("Primary Physician"))
                )
                //Should be the Id of the doctor
                .setMember(new Reference("Doctor_Id"));
        CareTeam careTeam = new CareTeam()
                .setIdentifier(
                        List.of(
                                new Identifier().setValue(data.getWardNo())
                        )
                )
                .setStatus(CareTeam.CareTeamStatus.ACTIVE)
                .setParticipant(List.of(participant));

        ResearchForm researchForm = new ResearchForm()
                .setElectronicConsent(data.getElectronicConsent())
                .setFilledBy(data.getFilledBy())
                .setPatientId(data.getPatientId())
                .setOccupation(data.getOccupation())
                .setEducation(data.getEducation())
                .setEthnicity(data.getEthnicity())
                .setFinances(data.getFinances());

        Bundle.BundleEntryComponent encEntry = new Bundle.BundleEntryComponent()
                .setResource(encounter);
        encEntry.getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Encounter");
        Bundle.BundleEntryComponent careEntry = new Bundle.BundleEntryComponent()
                .setResource(careTeam);
        careEntry.getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("CareTeam");

        Bundle bundle = new Bundle();
        bundle.setEntry(List.of(encEntry,careEntry));

        Bundle responseBundle = fhirManager.getClient()
                .transaction().withBundle(bundle).execute();

        researchForm = researchRepo.save(researchForm);

        for (Bundle.BundleEntryComponent responseEntry : responseBundle.getEntry()) {
            var response = responseEntry.getResponse();
            System.out.println("Created resource with ID: " + response.getId());
        }
        return researchForm;
    }
}
