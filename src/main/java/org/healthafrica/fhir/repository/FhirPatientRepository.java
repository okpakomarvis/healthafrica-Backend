package org.healthafrica.fhir.repository;

import org.healthafrica.fhir.entity.FhirPatient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FhirPatientRepository extends JpaRepository<FhirPatient, Long> {

    Optional<FhirPatient> findByIdAndTenantId(Long id, String tenantId);

    Optional<FhirPatient> findByTenantIdAndClientReferenceId(
            String tenantId,
            String clientReferenceId);

    List<FhirPatient> findByTenantId(String tenantId);

    List<FhirPatient> findByTenantIdAndCreatedByUserId(String tenantId, Long createdByUserId);

    long countByTenantId(String tenantId);
}
