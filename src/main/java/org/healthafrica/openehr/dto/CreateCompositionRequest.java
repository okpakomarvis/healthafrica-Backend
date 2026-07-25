package org.healthafrica.openehr.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request payload for creating an openEHR Composition.
 */
public record CreateCompositionRequest(
        @NotNull Long patientId,
        @NotBlank String archetypeId,
        @NotBlank String content
) {
}
