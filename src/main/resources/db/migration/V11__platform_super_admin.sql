ALTER TABLE tenant
    ADD COLUMN IF NOT EXISTS enabled BOOLEAN NOT NULL DEFAULT true;

ALTER TABLE tenant
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP NOT NULL DEFAULT NOW();

ALTER TABLE tenant
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP NOT NULL DEFAULT NOW();

UPDATE tenant SET enabled = true WHERE enabled IS NULL;

INSERT INTO tenant (code, name, enabled) VALUES
    ('PLATFORM', 'Platform Administration', true)
ON CONFLICT (code) DO NOTHING;

-- password: password
INSERT INTO app_user (username, password, tenant_id, role, enabled)
SELECT 'superadmin',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyM7M9wS2jLQk0iQF5EYh3YOEoTP0TOZGa',
       'PLATFORM',
       'SUPER_ADMIN',
       true
WHERE NOT EXISTS (
    SELECT 1 FROM app_user WHERE tenant_id = 'PLATFORM' AND username = 'superadmin'
);
