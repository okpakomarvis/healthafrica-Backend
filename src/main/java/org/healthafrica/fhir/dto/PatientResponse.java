package org.healthafrica.fhir.dto;

import java.time.LocalDate;

/**
 * FHIR Patient resource response.
 */
public record PatientResponse(
        Long id,
        String tenantId,
        String firstName,
        String lastName,
        String gender,
        LocalDate birthDate,
        Integer version,
        String clientReferenceId,
        Long createdByUserId,
        String createdByUsername,
        Double latitude,
        Double longitude
) {
}
