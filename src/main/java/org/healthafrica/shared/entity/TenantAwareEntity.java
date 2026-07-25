package org.healthafrica.shared.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(TenantEntityListener.class)
public abstract class TenantAwareEntity {

    @Column(name = "tenant_id")
    private String tenantId;
}