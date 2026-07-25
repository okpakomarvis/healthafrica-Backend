package org.healthafrica.openehr.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.healthafrica.openehr.dto.CompositionResponse;
import org.healthafrica.openehr.dto.CreateCompositionRequest;
import org.healthafrica.openehr.service.CompositionService;
import org.springframework.web.bind.annotation.*;

/**
 * REST endpoints for openEHR Composition resources.
 */
@RestController
@RequestMapping("/api/openehr/compositions")
@RequiredArgsConstructor
@Tag(name = "openEHR", description = "openEHR Composition resources")
public class CompositionController {

    private final CompositionService service;

    @PostMapping
    @Operation(summary = "Create composition", description = "Stores a new openEHR composition.")
    public CompositionResponse create(
            @Valid @RequestBody CreateCompositionRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get composition", description = "Returns an openEHR composition by ID.")
    public CompositionResponse get(
            @Parameter(description = "Composition ID") @PathVariable Long id) {
        return service.get(id);
    }
}
