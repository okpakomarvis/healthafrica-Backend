CREATE TABLE sync_conflict (
    id              BIGSERIAL PRIMARY KEY,
    tenant_id       VARCHAR(100) NOT NULL,
    local_id        VARCHAR(255),
    server_id       BIGINT,
    operation_type  VARCHAR(100),
    client_version  BIGINT,
    server_version  BIGINT,
    reason          VARCHAR(500),
    payload         JSONB,
    resolved        BOOLEAN DEFAULT FALSE,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_sync_conflict_tenant ON sync_conflict (tenant_id);
CREATE INDEX idx_sync_conflict_local  ON sync_conflict (local_id);
