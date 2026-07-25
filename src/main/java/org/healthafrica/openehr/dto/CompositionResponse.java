package org.healthafrica.openehr.dto;

import java.time.Instant;

public record CompositionResponse(

        Long id,

        String tenantId,

        String archetypeId,

        Long patientId,

        String content,

        Instant timestamp,

        Integer version

) {
}