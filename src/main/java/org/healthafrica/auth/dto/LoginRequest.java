package org.healthafrica.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Login credentials submitted by the client.
 */
public record LoginRequest(
        @NotBlank String username,
        @NotBlank String password
) {
}
