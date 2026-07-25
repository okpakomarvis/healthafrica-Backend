package org.healthafrica.communityevents.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.healthafrica.communityevents.dto.CreateVaccinationRequest;
import org.healthafrica.communityevents.dto.VaccinationResponse;
import org.healthafrica.communityevents.service.VaccinationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST endpoints for community vaccination events.
 */
@RestController
@RequestMapping("/api/events/vaccinations")
@RequiredArgsConstructor
@Tag(name = "Vaccinations", description = "Community vaccination events")
public class VaccinationController {

    private final VaccinationService service;

    @PostMapping
    @Operation(summary = "Record vaccination", description = "Creates a vaccination event with optional GPS coordinates.")
    public VaccinationResponse create(
            @Valid @RequestBody CreateVaccinationRequest request) {
        return service.create(request);
    }

    @GetMapping
    @Operation(summary = "List vaccinations", description = "Returns all vaccination events for the current tenant.")
    public List<VaccinationResponse> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vaccination", description = "Returns a vaccination event by ID.")
    public VaccinationResponse get(
            @Parameter(description = "Vaccination event ID") @PathVariable Long id) {
        return service.get(id);
    }
}
