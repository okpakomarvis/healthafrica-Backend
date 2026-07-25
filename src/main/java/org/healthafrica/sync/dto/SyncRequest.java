package org.healthafrica.sync.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * Batch sync request containing multiple offline operations.
 */
public record SyncRequest(
        @NotEmpty @Valid List<SyncOperation> operations
) {
}
