package org.healthafrica.tenant.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateTenantEnabledRequest(
        @NotNull Boolean enabled
) {
}
