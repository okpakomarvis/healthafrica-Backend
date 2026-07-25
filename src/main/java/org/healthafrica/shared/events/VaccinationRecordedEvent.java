package org.healthafrica.shared.events;

/**
 * Published when a community vaccination event is recorded.
 */
public record VaccinationRecordedEvent(
        Long vaccinationId,
        Long patientId,
        String vaccineName,
        String tenantId,
        String userId
) {
}
