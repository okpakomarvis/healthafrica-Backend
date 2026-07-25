package org.healthafrica.fhir.repository;

import org.healthafrica.fhir.entity.FhirObservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for FHIR Observation resources with tenant isolation.
 */
public interface FhirObservationRepository extends JpaRepository<FhirObservation, Long> {

    Optional<FhirObservation> findByIdAndTenantId(Long id, String tenantId);
}
