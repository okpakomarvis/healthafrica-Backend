package org.healthafrica.tenant.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.healthafrica.tenant.dto.*;
import org.healthafrica.tenant.service.TenantService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platform/tenants")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN')")
@Tag(name = "Platform Tenants", description = "Platform-level tenant lifecycle (SUPER_ADMIN)")
public class PlatformTenantController {

    private final TenantService tenantService;

    @GetMapping
    @Operation(summary = "List tenants", description = "Returns all organization tenants (excludes PLATFORM).")
    public List<TenantResponse> list() {
        return tenantService.listAll();
    }

    @PostMapping
    @Operation(summary = "Create tenant", description = "Creates a new organization tenant.")
    public TenantResponse create(@Valid @RequestBody CreateTenantRequest request) {
        return tenantService.create(request);
    }

    @PatchMapping("/{code}/enabled")
    @Operation(summary = "Enable or disable tenant", description = "Toggles whether a tenant can log in and use APIs.")
    public TenantResponse updateEnabled(
            @Parameter(description = "Tenant code") @PathVariable String code,
            @Valid @RequestBody UpdateTenantEnabledRequest request) {
        return tenantService.updateEnabled(code, request);
    }
}
