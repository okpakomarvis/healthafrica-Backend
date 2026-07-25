package org.healthafrica.communityevents.dto;

import java.time.LocalDate;

/**
 * Vaccination event response payload.
 */
public record VaccinationResponse(
        Long id,
        Long patientId,
        String vaccineName,
        LocalDate dateAdministered,
        Double latitude,
        Double longitude,
        Long version
) {
}
