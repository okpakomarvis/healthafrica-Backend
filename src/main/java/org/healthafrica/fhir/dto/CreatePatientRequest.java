package org.healthafrica.fhir.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Request payload for creating a FHIR Patient resource.
 */
public record CreatePatientRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        String gender,
        @NotNull LocalDate birthDate,
        String clientReferenceId,
        Double latitude,
        Double longitude
) {
    public CreatePatientRequest(
            String firstName,
            String lastName,
            String gender,
            LocalDate birthDate,
            String clientReferenceId) {
        this(firstName, lastName, gender, birthDate, clientReferenceId, null, null);
    }
}
