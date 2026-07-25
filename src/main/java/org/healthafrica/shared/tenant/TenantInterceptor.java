package org.healthafrica.shared.tenant;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * MVC interceptor that ensures a tenant is present for controller requests.
 * Tenant resolution is primarily handled by {@link TenantFilter}.
 */
@Component
public class TenantInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        if (TenantContextHolder.getTenant() == null
                || TenantContextHolder.getTenant().isBlank()) {
            throw new org.healthafrica.shared.exception.MissingTenantException(
                    "Missing X-Tenant-ID header");
        }

        return true;
    }
}
