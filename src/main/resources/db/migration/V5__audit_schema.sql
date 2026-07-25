CREATE TABLE audit_log (
    id         BIGSERIAL PRIMARY KEY,
    user_id    VARCHAR(255),
    tenant_id  VARCHAR(100),
    action     VARCHAR(255),
    timestamp  TIMESTAMP
);

CREATE INDEX idx_audit_tenant    ON audit_log (tenant_id);
CREATE INDEX idx_audit_timestamp ON audit_log (timestamp);
