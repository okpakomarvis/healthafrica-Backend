package org.healthafrica.sync.service;

import org.healthafrica.sync.exeception.SyncConflictException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ConflictDetector} version validation.
 */
class ConflictDetectorTest {

    private final ConflictDetector detector = new ConflictDetector(null, null);

    @Test
    void allowsMatchingVersions() {
        assertDoesNotThrow(() -> detector.validate(2L, 2L));
    }

    @Test
    void allowsNullClientVersion() {
        assertDoesNotThrow(() -> detector.validate(null, 5L));
    }

    @Test
    void throwsOnVersionMismatch() {
        assertThrows(
                SyncConflictException.class,
                () -> detector.validate(1L, 2L));
    }
}
