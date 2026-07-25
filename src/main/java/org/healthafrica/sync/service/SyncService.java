package org.healthafrica.sync.service;

import lombok.RequiredArgsConstructor;
import org.healthafrica.auth.security.SecurityUtils;
import org.healthafrica.shared.events.SyncCompletedEvent;
import org.healthafrica.shared.tenant.TenantContextHolder;
import org.healthafrica.sync.dto.*;
import org.healthafrica.sync.exeception.SyncConflictException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Orchestrates batch offline synchronization with optimistic locking and conflict detection.
 */
@Service
@RequiredArgsConstructor
public class SyncService {

    private final SyncProcessor processor;
    private final ApplicationEventPublisher publisher;

    public SyncResponse synchronize(SyncRequest request) {
        List<SyncResult> results = new ArrayList<>();
        int processed = 0;

        for (SyncOperation operation : request.operations()) {
            try {
                Long serverId = processor.process(operation);
                processed++;

                results.add(new SyncResult(
                        operation.localId(),
                        serverId,
                        SyncStatus.SUCCESS,
                        "SYNC_SUCCESS"));

            } catch (SyncConflictException ex) {
                results.add(new SyncResult(
                        operation.localId(),
                        operation.serverId(),
                        SyncStatus.CONFLICT,
                        ex.getMessage()));

            } catch (Exception ex) {
                results.add(new SyncResult(
                        operation.localId(),
                        operation.serverId(),
                        SyncStatus.FAILED,
                        ex.getMessage()));
            }
        }

        publisher.publishEvent(new SyncCompletedEvent(
                TenantContextHolder.getTenant(),
                processed,
                SecurityUtils.currentUserId()));

        return new SyncResponse(results);
    }
}
