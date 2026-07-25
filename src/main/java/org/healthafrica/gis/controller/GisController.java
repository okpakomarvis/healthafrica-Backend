package org.healthafrica.gis.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.healthafrica.gis.dto.ClinicResponse;
import org.healthafrica.gis.dto.GeoJsonResponse;
import org.healthafrica.gis.service.GisService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * GIS endpoints for vaccination maps and clinic proximity search.
 */
@RestController
@RequestMapping("/api/gis")
@RequiredArgsConstructor
@Tag(name = "GIS", description = "Geospatial maps and clinic proximity")
public class GisController {

    private final GisService service;

    @GetMapping("/vaccination-map")
    @Operation(summary = "Vaccination map", description = "Returns GeoJSON vaccination event locations.")
    public GeoJsonResponse map() {
        return service.vaccinationMap();
    }

    @GetMapping("/nearby-clinics")
    @Operation(summary = "Nearby clinics", description = "Finds clinics within a radius of the given coordinates.")
    public List<ClinicResponse> nearbyClinics(
            @Parameter(description = "Latitude") @RequestParam double latitude,
            @Parameter(description = "Longitude") @RequestParam double longitude,
            @Parameter(description = "Search radius in meters") @RequestParam(defaultValue = "5000") double radiusMeters) {
        return service.nearbyClinics(latitude, longitude, radiusMeters);
    }
}
