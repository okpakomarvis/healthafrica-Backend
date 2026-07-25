package org.healthafrica.campaign.dto;

import java.time.LocalDate;

public record UpdateCampaignSiteRequest(
        String name,
        String description,
        Double latitude,
        Double longitude,
        LocalDate activeFrom,
        LocalDate activeTo,
        Boolean enabled
) {
}
