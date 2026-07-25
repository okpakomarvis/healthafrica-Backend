package org.healthafrica.sync.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

/**
 * Persisted record of a sync conflict detected during offline synchronization.
 */
@Getter
@Setter
@Entity
@Table(name = "sync_conflict")
public class SyncConflict {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    private String localId;

    private Long serverId;

    private String operationType;

    private Long clientVersion;

    private Long serverVersion;

    private String reason;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String payload;

    private boolean resolved;

    private Instant createdAt = Instant.now();
}
