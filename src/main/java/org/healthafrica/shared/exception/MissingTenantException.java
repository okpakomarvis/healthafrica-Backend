package org.healthafrica.shared.exception;

/**
 * Thrown when the required {@code X-Tenant-ID} header is absent.
 */
public class MissingTenantException extends RuntimeException {

    public MissingTenantException(String message) {
        super(message);
    }
}
