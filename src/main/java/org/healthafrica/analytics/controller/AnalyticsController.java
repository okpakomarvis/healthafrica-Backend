package org.healthafrica.analytics.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.healthafrica.analytics.dto.AnalyticsSummaryResponse;
import org.healthafrica.analytics.dto.UserRegistrationStats;
import org.healthafrica.analytics.service.AnalyticsService;
import org.healthafrica.gis.dto.GeoJsonResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@Tag(name = "Analytics", description = "Tenant analytics and registration overview")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/summary")
    @Operation(summary = "Analytics summary", description = "Returns high-level counts for the current tenant.")
    public AnalyticsSummaryResponse summary() {
        return analyticsService.summary();
    }

    @GetMapping("/registrations-by-user")
    @Operation(
            summary = "Registrations by user",
            description = "Returns patient and vaccination counts attributed to each user.")
    public List<UserRegistrationStats> registrationsByUser() {
        return analyticsService.registrationsByUser();
    }

    @GetMapping("/patient-map")
    @Operation(
            summary = "Patient map",
            description = "Returns GeoJSON patient locations. Optionally filter by registering user.")
    public GeoJsonResponse patientMap(
            @Parameter(description = "Optional user ID filter") @RequestParam(required = false) Long userId) {
        return analyticsService.patientMap(userId);
    }

    @GetMapping("/vaccination-map")
    @Operation(
            summary = "Vaccination analytics map",
            description = "Returns GeoJSON vaccination locations. Optionally filter by recording user.")
    public GeoJsonResponse vaccinationMap(
            @Parameter(description = "Optional user ID filter") @RequestParam(required = false) Long userId) {
        return analyticsService.vaccinationMap(userId);
    }
}
