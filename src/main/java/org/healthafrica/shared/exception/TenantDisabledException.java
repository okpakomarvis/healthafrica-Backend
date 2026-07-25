package org.healthafrica.shared.exception;

public class TenantDisabledException extends RuntimeException {

    public TenantDisabledException(String message) {
        super(message);
    }
}
