package org.healthafrica.notification.dto;

import java.time.Instant;

public record NotificationMessage(

        String tenantId,

        String eventType,

        String message,

        Instant timestamp

) {
}