package org.healthafrica.tenant.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.healthafrica.tenant.dto.PublicTenantResponse;
import org.healthafrica.tenant.service.TenantService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User authentication and JWT issuance")
public class PublicTenantController {

    private final TenantService tenantService;

    @GetMapping("/tenants")
    @SecurityRequirements
    @Operation(
            summary = "List enabled tenants",
            description = "Returns enabled organization tenants available for login. No authentication required.")
    public List<PublicTenantResponse> listEnabledTenants() {
        return tenantService.listEnabledForLogin();
    }
}
