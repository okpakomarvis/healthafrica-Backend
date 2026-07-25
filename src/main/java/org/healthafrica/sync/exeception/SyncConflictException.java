package org.healthafrica.sync.exeception;

public class SyncConflictException
        extends RuntimeException {

    public SyncConflictException(
            String message) {

        super(message);
    }
}
