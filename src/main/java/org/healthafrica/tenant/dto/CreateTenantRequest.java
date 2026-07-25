package org.healthafrica.tenant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateTenantRequest(
        @NotBlank
        @Size(max = 100)
        @Pattern(regexp = "^[A-Z][A-Z0-9_]*$", message = "Code must be uppercase letters, numbers, or underscores")
        String code,
        @NotBlank
        @Size(max = 255)
        String name
) {
}
