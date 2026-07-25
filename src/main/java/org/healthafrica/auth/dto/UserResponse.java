package org.healthafrica.auth.dto;

import org.healthafrica.auth.entity.Role;

import java.time.Instant;

public record UserResponse(
        Long id,
        String username,
        String tenantId,
        Role role,
        boolean enabled,
        Instant createdAt,
        Instant updatedAt
) {
}
