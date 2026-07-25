package org.healthafrica.campaign.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateCampaignSiteRequest(
        @NotBlank String name,
        String description,
        @NotNull Double latitude,
        @NotNull Double longitude,
        LocalDate activeFrom,
        LocalDate activeTo
) {
}
