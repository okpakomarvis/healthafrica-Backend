package org.healthafrica.tenant.dto;

import org.healthafrica.tenant.entity.SubscriptionStatus;

import java.time.Instant;

public record TenantResponse(
        Long id,
        String code,
        String name,
        boolean enabled,
        SubscriptionStatus subscriptionStatus,
        Instant createdAt,
        Instant updatedAt
) {
}
