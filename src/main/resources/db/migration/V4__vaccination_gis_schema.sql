CREATE TABLE vaccination_event (
    id                BIGSERIAL PRIMARY KEY,
    tenant_id         VARCHAR(100) NOT NULL,
    patient_id        BIGINT NOT NULL,
    vaccine_name      VARCHAR(255),
    date_administered DATE,
    location          geometry(Point, 4326),
    version           BIGINT DEFAULT 0,
    created_at        TIMESTAMP,
    updated_at        TIMESTAMP
);

CREATE INDEX idx_vaccination_geom    ON vaccination_event USING GIST (location);
CREATE INDEX idx_vaccination_tenant  ON vaccination_event (tenant_id);
CREATE INDEX idx_vaccination_patient ON vaccination_event (patient_id);

CREATE TABLE clinic (
    id         BIGSERIAL PRIMARY KEY,
    tenant_id  VARCHAR(100) NOT NULL,
    name       VARCHAR(255) NOT NULL,
    location   geometry(Point, 4326) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_clinic_geom   ON clinic USING GIST (location);
CREATE INDEX idx_clinic_tenant ON clinic (tenant_id);

INSERT INTO clinic (tenant_id, name, location, created_at, updated_at) VALUES
    ('NGO_A', 'Lagos Mainland Clinic', ST_SetSRID(ST_MakePoint(3.3792, 6.5244), 4326), NOW(), NOW()),
    ('NGO_A', 'Ikeja Health Centre', ST_SetSRID(ST_MakePoint(3.3421, 6.6018), 4326), NOW(), NOW()),
    ('NGO_A', 'Surulere Community Clinic', ST_SetSRID(ST_MakePoint(3.3569, 6.4969), 4326), NOW(), NOW());
