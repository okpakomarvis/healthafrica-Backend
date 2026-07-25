package org.healthafrica.shared.events;

/**
 * Published when a new FHIR Patient resource is created.
 */
public record PatientCreatedEvent(
        Long patientId,
        String tenantId,
        String userId
) {
}
