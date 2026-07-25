package org.healthafrica.communityevents.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Request payload for recording a community vaccination event.
 */
public record CreateVaccinationRequest(
        @NotNull Long patientId,
        @NotBlank String vaccineName,
        @NotNull LocalDate dateAdministered,
        @NotNull Double latitude,
        @NotNull Double longitude
) {
}
