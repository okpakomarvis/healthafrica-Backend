package org.healthafrica.fhir.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Request payload for updating a FHIR Patient resource.
 */
public record UpdatePatientRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        String gender,
        @NotNull LocalDate birthDate
) {
}
