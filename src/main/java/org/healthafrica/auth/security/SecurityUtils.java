package org.healthafrica.auth.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility methods for accessing the current authenticated user.
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Long currentUserIdLong() {
        return getAuthenticatedUser()
                .map(AuthenticatedUser::userId)
                .orElse(null);
    }

    public static String currentUserId() {
        return getAuthenticatedUser()
                .map(user -> user.userId().toString())
                .orElse("SYSTEM");
    }

    public static String currentTenantId() {
        return getAuthenticatedUser()
                .map(AuthenticatedUser::tenantId)
                .orElse(null);
    }

    public static String currentRole() {
        return getAuthenticatedUser()
                .map(AuthenticatedUser::role)
                .orElse(null);
    }

    public static boolean isSuperAdmin() {
        return "SUPER_ADMIN".equals(currentRole());
    }

    private static java.util.Optional<AuthenticatedUser> getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser user)) {
            return java.util.Optional.empty();
        }

        return java.util.Optional.of(user);
    }
}
