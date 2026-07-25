package org.healthafrica.sync.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.healthafrica.sync.dto.SyncOperation;
import org.healthafrica.sync.entity.SyncConflict;
import org.healthafrica.sync.exeception.SyncConflictException;
import org.healthafrica.sync.repository.SyncConflictRepository;
import org.springframework.stereotype.Component;

/**
 * Detects version conflicts and persists conflict records for offline sync operations.
 */
@Component
@RequiredArgsConstructor
public class ConflictDetector {

    private final SyncConflictRepository conflictRepository;
    private final ObjectMapper objectMapper;

    public void validate(Long clientVersion, Long serverVersion) {
        if (clientVersion == null) {
            return;
        }

        if (!clientVersion.equals(serverVersion)) {
            throw new SyncConflictException("VERSION_CONFLICT");
        }
    }

    public void recordConflict(
            String tenantId,
            SyncOperation operation,
            Long serverVersion,
            String reason) {

        SyncConflict conflict = new SyncConflict();
        conflict.setTenantId(tenantId);
        conflict.setLocalId(operation.localId());
        conflict.setServerId(operation.serverId());
        conflict.setOperationType(operation.operationType().name());
        conflict.setClientVersion(operation.version());
        conflict.setServerVersion(serverVersion);
        conflict.setReason(reason);

        try {
            conflict.setPayload(objectMapper.writeValueAsString(operation.payload()));
        } catch (Exception ex) {
            conflict.setPayload("{}");
        }

        conflictRepository.save(conflict);
    }
}
