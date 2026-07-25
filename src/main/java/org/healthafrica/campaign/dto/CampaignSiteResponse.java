package org.healthafrica.campaign.dto;

import java.time.Instant;
import java.time.LocalDate;

public record CampaignSiteResponse(
        Long id,
        String tenantId,
        String name,
        String description,
        Double latitude,
        Double longitude,
        LocalDate activeFrom,
        LocalDate activeTo,
        boolean enabled,
        Long createdByUserId,
        Instant createdAt
) {
}
