package org.healthafrica.audit.service;

import lombok.RequiredArgsConstructor;
import org.healthafrica.audit.entity.AuditLog;
import org.healthafrica.audit.repository.AuditLogRepository;
import org.healthafrica.shared.events.PatientCreatedEvent;
import org.healthafrica.shared.events.SyncCompletedEvent;
import org.healthafrica.shared.events.VaccinationRecordedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Listens to domain events and persists audit log entries.
 */
@Component
@RequiredArgsConstructor
public class AuditEventListener {

    private final AuditLogRepository repository;

    @EventListener
    public void handle(PatientCreatedEvent event) {
        save(event.userId(), event.tenantId(),
                "PATIENT_CREATED:" + event.patientId());
    }

    @EventListener
    public void handle(VaccinationRecordedEvent event) {
        save(event.userId(), event.tenantId(),
                "VACCINATION_RECORDED:" + event.vaccinationId());
    }

    @EventListener
    public void handle(SyncCompletedEvent event) {
        save(event.userId(), event.tenantId(),
                "SYNC_COMPLETED:" + event.processedOperations());
    }

    private void save(String userId, String tenantId, String action) {
        AuditLog log = new AuditLog();
        log.setUserId(userId != null ? userId : "SYSTEM");
        log.setTenantId(tenantId);
        log.setAction(action);
        log.setTimestamp(Instant.now());
        repository.save(log);
    }
}
