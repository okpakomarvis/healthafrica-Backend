package org.healthafrica.campaign.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.healthafrica.campaign.dto.*;
import org.healthafrica.campaign.service.CampaignSiteService;
import org.healthafrica.gis.dto.GeoJsonResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaigns/sites")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('PROGRAM_MANAGER', 'ADMIN')")
@Tag(name = "Campaign Sites", description = "Vaccination campaign registration sites")
public class CampaignSiteController {

    private final CampaignSiteService service;

    @GetMapping
    @Operation(summary = "List campaign sites", description = "Returns all campaign sites for the current tenant.")
    public List<CampaignSiteResponse> list() {
        return service.list();
    }

    @GetMapping("/map")
    @Operation(summary = "Campaign sites map", description = "Returns GeoJSON features for campaign site locations.")
    public GeoJsonResponse map() {
        return service.mapSites();
    }

    @PostMapping
    @Operation(summary = "Create campaign site", description = "Registers a new vaccination campaign site.")
    public CampaignSiteResponse create(@Valid @RequestBody CreateCampaignSiteRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update campaign site", description = "Updates an existing campaign site.")
    public CampaignSiteResponse update(
            @Parameter(description = "Campaign site ID") @PathVariable Long id,
            @Valid @RequestBody UpdateCampaignSiteRequest request) {
        return service.update(id, request);
    }
}
