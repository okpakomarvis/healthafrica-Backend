package org.healthafrica.communityevents.repository;

import org.healthafrica.communityevents.entity.VaccinationEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for vaccination events with tenant-scoped queries.
 */
public interface VaccinationRepository extends JpaRepository<VaccinationEvent, Long> {

    List<VaccinationEvent> findByTenantId(String tenantId);

    Optional<VaccinationEvent> findByIdAndTenantId(Long id, String tenantId);
}
