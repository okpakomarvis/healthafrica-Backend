package org.healthafrica.fhir.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.healthafrica.fhir.dto.CreateObservationRequest;
import org.healthafrica.fhir.dto.ObservationResponse;
import org.healthafrica.fhir.service.FhirObservationService;
import org.springframework.web.bind.annotation.*;

/**
 * REST endpoints for FHIR Observation resources.
 */
@RestController
@RequestMapping("/api/fhir/observations")
@RequiredArgsConstructor
@Tag(name = "FHIR Observations", description = "FHIR Observation resources")
public class FhirObservationController {

    private final FhirObservationService service;

    @PostMapping
    @Operation(summary = "Create observation", description = "Creates a new FHIR Observation resource.")
    public ObservationResponse create(
            @Valid @RequestBody CreateObservationRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get observation", description = "Returns an observation by ID.")
    public ObservationResponse get(
            @Parameter(description = "Observation ID") @PathVariable Long id) {
        return service.get(id);
    }
}
