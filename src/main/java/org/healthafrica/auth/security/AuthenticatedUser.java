package org.healthafrica.auth.security;

/**
 * Authenticated user principal stored in the Spring Security context.
 */
public record AuthenticatedUser(Long userId, String tenantId, String role) {
}
