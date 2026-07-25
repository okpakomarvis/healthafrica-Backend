package org.healthafrica.sync.repository;

import org.healthafrica.sync.entity.SyncConflict;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for persisted sync conflict records.
 */
public interface SyncConflictRepository extends JpaRepository<SyncConflict, Long> {

    List<SyncConflict> findByTenantId(String tenantId);
}
