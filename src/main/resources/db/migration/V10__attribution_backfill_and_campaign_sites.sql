-- Backfill patient attribution from audit trail
UPDATE fhir_patient p
SET created_by_user_id = CAST(a.user_id AS BIGINT)
FROM audit_log a
WHERE p.created_by_user_id IS NULL
  AND a.action = 'PATIENT_CREATED:' || p.id::text
  AND a.tenant_id = p.tenant_id
  AND a.user_id ~ '^[0-9]+$';

-- Backfill vaccination attribution from audit trail
UPDATE vaccination_event v
SET recorded_by_user_id = CAST(a.user_id AS BIGINT)
FROM audit_log a
WHERE v.recorded_by_user_id IS NULL
  AND a.action = 'VACCINATION_RECORDED:' || v.id::text
  AND a.tenant_id = v.tenant_id
  AND a.user_id ~ '^[0-9]+$';

CREATE TABLE vaccination_campaign_site (
    id                 BIGSERIAL PRIMARY KEY,
    tenant_id          VARCHAR(100) NOT NULL,
    name               VARCHAR(200) NOT NULL,
    description        TEXT,
    location           geometry(Point, 4326) NOT NULL,
    active_from        DATE,
    active_to          DATE,
    enabled            BOOLEAN NOT NULL DEFAULT true,
    created_by_user_id BIGINT REFERENCES app_user (id),
    created_at         TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_campaign_site_tenant ON vaccination_campaign_site (tenant_id);
