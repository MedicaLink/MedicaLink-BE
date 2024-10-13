package com.medicalink.MedicaLink_backend.controllers;

import com.medicalink.MedicaLink_backend.dto.AddResearchFormDto;
import com.medicalink.MedicaLink_backend.models.ResearchForm;
import com.medicalink.MedicaLink_backend.services.ResearchService;
import com.medicalink.MedicaLink_backend.utils.ApiError;
import com.medicalink.MedicaLink_backend.utils.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/research")
@RestController
public class ResearchController {
    private final ResearchService researchService;

    public ResearchController(ResearchService researchService) {
        this.researchService = researchService;
    }

    @PreAuthorize("hasRole('PRACTITIONER')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<String,ApiError>> addResearchData(@RequestBody AddResearchFormDto input) {
        ResearchForm researchForm = researchService.addResearchForm(input);

        if(researchForm.getId() == null) {
            return ResponseEntity.ok(new ApiResponse<>(null, new ApiError("Could not save data!")));
        }
        return ResponseEntity.ok(new ApiResponse<>("Research form added.", null));
    }
}
