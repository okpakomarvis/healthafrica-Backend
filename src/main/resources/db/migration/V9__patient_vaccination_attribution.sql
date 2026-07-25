ALTER TABLE fhir_patient
    ADD COLUMN IF NOT EXISTS created_by_user_id BIGINT REFERENCES app_user (id);

ALTER TABLE fhir_patient
    ADD COLUMN IF NOT EXISTS location geometry(Point, 4326);

ALTER TABLE vaccination_event
    ADD COLUMN IF NOT EXISTS recorded_by_user_id BIGINT REFERENCES app_user (id);

CREATE INDEX IF NOT EXISTS idx_fhir_patient_created_by ON fhir_patient (created_by_user_id);

CREATE INDEX IF NOT EXISTS idx_vaccination_recorded_by ON vaccination_event (recorded_by_user_id);
