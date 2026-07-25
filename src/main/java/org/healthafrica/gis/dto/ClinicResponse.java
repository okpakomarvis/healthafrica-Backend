package org.healthafrica.gis.dto;

/**
 * Nearby clinic search result.
 */
public record ClinicResponse(
        Long id,
        String name,
        double latitude,
        double longitude
) {
}
