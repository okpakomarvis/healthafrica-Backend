package org.healthafrica.sync.dto;

public record SyncResult(

        String localId,

        Long serverId,

        SyncStatus status,

        String message

) {
}