package com.medicalink.MedicaLink_backend.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FhirManager {
    private final String serverBase="http://localhost:8080/fhir";

    private FhirContext context;
    private IGenericClient client;

    public FhirManager() {
        context = FhirContext.forR4();
        client = context.newRestfulGenericClient(serverBase);
    }

    public FhirContext getContext() {
        return context;
    }

    public IGenericClient getClient() {
        return client;
    }
}
