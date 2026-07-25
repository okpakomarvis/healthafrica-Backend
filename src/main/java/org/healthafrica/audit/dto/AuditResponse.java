package org.healthafrica.audit.dto;


import java.time.Instant;

public record AuditResponse(

        Long id,

        String userId,

        String tenantId,

        String action,

        Instant timestamp

) {
}
