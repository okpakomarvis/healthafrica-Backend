package org.healthafrica.analytics.dto;

public record AnalyticsSummaryResponse(
        long userCount,
        long patientCount,
        long vaccinationCount
) {
}
