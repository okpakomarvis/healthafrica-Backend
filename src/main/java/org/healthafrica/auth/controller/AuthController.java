package org.healthafrica.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.healthafrica.auth.dto.LoginRequest;
import org.healthafrica.auth.dto.LoginResponse;
import org.healthafrica.auth.entity.User;
import org.healthafrica.auth.repository.UserRepository;
import org.healthafrica.auth.security.JwtService;
import org.healthafrica.shared.exception.TenantMismatchException;
import org.healthafrica.shared.tenant.TenantContextHolder;
import org.healthafrica.tenant.service.TenantService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication endpoints for JWT-based login.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User authentication and JWT issuance")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final TenantService tenantService;

    /**
     * Authenticates a user and returns a signed JWT.
     * Requires {@code X-Tenant-ID} header matching the user's tenant.
     */
    @PostMapping("/login")
    @SecurityRequirements
    @Operation(
            summary = "Login",
            description = "Authenticates with username and password. "
                    + "Requires the X-Tenant-ID header. Returns a signed JWT.",
            parameters = {
                    @Parameter(
                            name = "X-Tenant-ID",
                            description = "Tenant / organization subdomain code",
                            required = true,
                            in = ParameterIn.HEADER,
                            example = "NGO_A")
            })
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        String tenantId = TenantContextHolder.getTenant();
        tenantService.assertEnabledForLogin(tenantId);

        User user = userRepository
                .findByTenantIdAndUsername(tenantId, request.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!user.isEnabled()) {
            throw new BadCredentialsException("Account disabled");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        if (!user.getTenantId().equals(tenantId)) {
            throw new TenantMismatchException(
                    "X-Tenant-ID does not match user tenant");
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getTenantId(),
                user.getRole().name());

        return new LoginResponse(token);
    }
}
