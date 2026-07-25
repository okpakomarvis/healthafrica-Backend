package org.healthafrica.analytics.dto;

import org.healthafrica.auth.entity.Role;

public record UserRegistrationStats(
        Long userId,
        String username,
        Role role,
        long patientCount,
        long vaccinationCount
) {
}
