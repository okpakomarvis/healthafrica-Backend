package org.healthafrica.shared.tenant;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.healthafrica.auth.security.AuthenticatedUser;
import org.healthafrica.auth.security.SecurityUtils;
import org.healthafrica.platform.PlatformConstants;
import org.healthafrica.shared.exception.MissingTenantException;
import org.healthafrica.shared.exception.TenantDisabledException;
import org.healthafrica.shared.exception.TenantMismatchException;
import org.healthafrica.tenant.service.TenantService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Servlet filter that validates the {@code X-Tenant-ID} header and reconciles it with JWT claims.
 */
@Component
@RequiredArgsConstructor
public class TenantFilter extends OncePerRequestFilter {

    public static final String TENANT_HEADER = "X-Tenant-ID";

    private final TenantService tenantService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String tenant = request.getHeader(TENANT_HEADER);

        if (tenant == null || tenant.isBlank()) {
            throw new MissingTenantException("Missing X-Tenant-ID header");
        }

        TenantContextHolder.setTenant(tenant.trim());

        if (!SecurityUtils.isSuperAdmin()
                && !PlatformConstants.PLATFORM_TENANT.equals(tenant)
                && !tenantService.isEnabled(tenant)) {
            throw new TenantDisabledException("Tenant is disabled");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.getPrincipal() instanceof AuthenticatedUser user
                && !tenant.equals(user.tenantId())) {
            throw new TenantMismatchException(
                    "X-Tenant-ID does not match authenticated tenant");
        }

        String jwtTenant = SecurityUtils.currentTenantId();
        if (jwtTenant != null && !tenant.equals(jwtTenant)) {
            throw new TenantMismatchException(
                    "X-Tenant-ID does not match JWT tenant claim");
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContextHolder.clear();
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/actuator/health")
                || path.startsWith("/actuator/info")
                || path.equals("/api/auth/tenants")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui");
    }
}
