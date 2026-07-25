CREATE TABLE fhir_patient (
    id                   BIGSERIAL PRIMARY KEY,
    tenant_id            VARCHAR(100) NOT NULL,
    first_name           VARCHAR(100) NOT NULL,
    last_name            VARCHAR(100) NOT NULL,
    gender               VARCHAR(50),
    birth_date           DATE,
    version              INTEGER DEFAULT 0,
    client_reference_id  VARCHAR(255) NOT NULL,
    resource_json        JSONB NOT NULL DEFAULT '{}',
    created_at           TIMESTAMP,
    updated_at           TIMESTAMP
);

CREATE INDEX idx_fhir_patient_tenant ON fhir_patient (tenant_id);

CREATE UNIQUE INDEX idx_patient_client_ref
    ON fhir_patient (tenant_id, client_reference_id);

CREATE TABLE fhir_observation (
    id                BIGSERIAL PRIMARY KEY,
    tenant_id         VARCHAR(100) NOT NULL,
    patient_id        BIGINT NOT NULL,
    observation_type  VARCHAR(100),
    value             VARCHAR(255),
    timestamp         TIMESTAMP,
    version           INTEGER DEFAULT 0,
    resource_json     JSONB NOT NULL DEFAULT '{}',
    created_at        TIMESTAMP,
    updated_at        TIMESTAMP
);

CREATE INDEX idx_fhir_observation_patient ON fhir_observation (patient_id);
CREATE INDEX idx_fhir_observation_tenant  ON fhir_observation (tenant_id);
