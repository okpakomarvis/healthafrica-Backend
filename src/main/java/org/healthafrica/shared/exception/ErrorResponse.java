package org.healthafrica.shared.exception;

import java.time.Instant;

/**
 * Standard API error response payload.
 */
public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
