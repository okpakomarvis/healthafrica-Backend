package org.healthafrica.openehr.repository;


import org.healthafrica.openehr.entity.Composition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompositionRepository
        extends JpaRepository<Composition, Long> {

    Optional<Composition> findByIdAndTenantId(
            Long id,
            String tenantId
    );
}