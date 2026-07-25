package org.healthafrica.fhir.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.healthafrica.fhir.dto.*;
import org.healthafrica.fhir.service.FhirPatientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST endpoints for FHIR Patient resources.
 */
@RestController
@RequestMapping("/api/fhir/patients")
@RequiredArgsConstructor
@Tag(name = "FHIR Patients", description = "FHIR Patient resources")
public class FhirPatientController {

    private final FhirPatientService service;

    @GetMapping
    @Operation(summary = "List patients", description = "Returns all patients for the current tenant.")
    public List<PatientResponse> list() {
        return service.list();
    }

    @PostMapping
    @Operation(summary = "Create patient", description = "Creates a new FHIR Patient resource.")
    public PatientResponse create(
            @Valid @RequestBody CreatePatientRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get patient", description = "Returns a patient by ID.")
    public PatientResponse get(
            @Parameter(description = "Patient ID") @PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update patient", description = "Updates an existing FHIR Patient resource.")
    public PatientResponse update(
            @Parameter(description = "Patient ID") @PathVariable Long id,
            @Valid @RequestBody UpdatePatientRequest request) {
        return service.update(id, request);
    }
}
