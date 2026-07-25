package org.healthafrica.shared.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.healthafrica.auth.security.SecurityUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Restricts SUPER_ADMIN to platform administration APIs only.
 */
@Component
public class PlatformAccessFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        if (SecurityUtils.isSuperAdmin() && !isAllowedPath(request.getRequestURI())) {
            throw new AccessDeniedException(
                    "Super admin cannot access tenant operational data");
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !SecurityUtils.isSuperAdmin();
    }

    private boolean isAllowedPath(String path) {
        return path.startsWith("/api/platform/")
                || path.startsWith("/api/auth/")
                || path.startsWith("/actuator/health")
                || path.startsWith("/actuator/info")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui");
    }
}
