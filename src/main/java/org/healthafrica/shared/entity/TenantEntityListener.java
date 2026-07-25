package org.healthafrica.shared.entity;

import jakarta.persistence.PrePersist;
import org.healthafrica.shared.tenant.TenantContextHolder;

/**
 * Automatically injects the active tenant into new tenant-aware entities.
 */
public class TenantEntityListener {

    @PrePersist
    public void setTenant(TenantAwareEntity entity) {
        if (entity.getTenantId() == null || entity.getTenantId().isBlank()) {
            entity.setTenantId(TenantContextHolder.getTenant());
        }
    }
}
