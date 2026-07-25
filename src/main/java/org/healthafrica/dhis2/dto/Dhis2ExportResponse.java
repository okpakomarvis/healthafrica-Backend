package org.healthafrica.dhis2.dto;

/**
 * Response from a DHIS2 export operation.
 */
public record Dhis2ExportResponse(
        boolean success,
        String message,
        int exportedCount
) {
}
