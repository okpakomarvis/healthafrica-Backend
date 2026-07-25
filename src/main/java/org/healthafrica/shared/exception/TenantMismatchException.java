package org.healthafrica.shared.exception;

/**
 * Thrown when the request tenant does not match the authenticated JWT tenant.
 */
public class TenantMismatchException extends RuntimeException {

    public TenantMismatchException(String message) {
        super(message);
    }
}
