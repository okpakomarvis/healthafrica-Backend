CREATE TABLE openehr_composition (
    id            BIGSERIAL PRIMARY KEY,
    tenant_id     VARCHAR(100) NOT NULL,
    archetype_id  VARCHAR(255) NOT NULL,
    patient_id    BIGINT NOT NULL,
    content       JSONB,
    version       INTEGER DEFAULT 0,
    timestamp     TIMESTAMP,
    created_at    TIMESTAMP,
    updated_at    TIMESTAMP
);

CREATE INDEX idx_openehr_patient   ON openehr_composition (patient_id);
CREATE INDEX idx_openehr_tenant    ON openehr_composition (tenant_id);
CREATE INDEX idx_openehr_archetype ON openehr_composition (archetype_id);
