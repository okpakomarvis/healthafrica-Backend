package org.healthafrica.dhis2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.healthafrica.dhis2.dto.Dhis2ExportResponse;
import org.healthafrica.dhis2.service.Dhis2ExportService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        "/api/integration/dhis2")
@RequiredArgsConstructor
@Tag(name = "DHIS2 Integration", description = "DHIS2 export integration")
public class Dhis2Controller {

    private final Dhis2ExportService service;

    @PostMapping("/export")
    @Operation(
            summary = "Export to DHIS2",
            description = "Builds and sends a DHIS2 payload for the current tenant (mock client in development).")
    public Dhis2ExportResponse export() {

        return service.export();
    }
}
