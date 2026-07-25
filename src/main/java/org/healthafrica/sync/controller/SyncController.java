package org.healthafrica.sync.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.healthafrica.sync.dto.SyncRequest;
import org.healthafrica.sync.dto.SyncResponse;
import org.healthafrica.sync.service.SyncService;
import org.springframework.web.bind.annotation.*;

/**
 * REST endpoint for offline batch synchronization.
 */
@RestController
@RequestMapping("/api/sync")
@RequiredArgsConstructor
@Tag(name = "Sync", description = "Offline batch synchronization")
public class SyncController {

    private final SyncService service;

    @PostMapping
    @Operation(
            summary = "Synchronize batch",
            description = "Processes a batch of offline create/update operations with optimistic locking.")
    public SyncResponse synchronize(
            @Valid @RequestBody SyncRequest request) {
        return service.synchronize(request);
    }
}
