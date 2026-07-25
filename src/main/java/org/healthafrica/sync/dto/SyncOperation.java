package org.healthafrica.sync.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Map;

/**
 * A single offline sync operation from a mobile client.
 */
public record SyncOperation(
        String localId,
        String clientReferenceId,
        String parentLocalId,
        Long serverId,
        Long version,
        @NotNull OperationType operationType,
        @NotNull Map<String, Object> payload
) {
}
