package org.healthafrica.fhir.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request payload for creating a FHIR Observation resource.
 */
public record CreateObservationRequest(
        @NotNull Long patientId,
        @NotBlank String observationType,
        @NotBlank String value
) {
}
