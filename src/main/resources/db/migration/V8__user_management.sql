ALTER TABLE app_user
    ADD COLUMN IF NOT EXISTS enabled BOOLEAN NOT NULL DEFAULT true;

ALTER TABLE app_user
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE app_user
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE app_user DROP CONSTRAINT IF EXISTS app_user_username_key;

CREATE UNIQUE INDEX IF NOT EXISTS uk_app_user_tenant_username
    ON app_user (tenant_id, username);

CREATE INDEX IF NOT EXISTS idx_app_user_tenant ON app_user (tenant_id);
