package org.healthafrica.shared.events;

/**
 * Published after a batch of offline sync operations has been processed.
 */
public record SyncCompletedEvent(
        String tenantId,
        Integer processedOperations,
        String userId
) {
}
