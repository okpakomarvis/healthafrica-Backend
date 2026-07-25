CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TABLE tenant (
    id       BIGSERIAL PRIMARY KEY,
    code     VARCHAR(100) UNIQUE NOT NULL,
    name     VARCHAR(255)        NOT NULL
);

CREATE TABLE app_user (
    id        BIGSERIAL PRIMARY KEY,
    username  VARCHAR(255) NOT NULL UNIQUE,
    password  VARCHAR(255) NOT NULL,
    tenant_id VARCHAR(255) NOT NULL,
    role      VARCHAR(100) NOT NULL
);

CREATE INDEX idx_user_tenant ON app_user (tenant_id);

INSERT INTO tenant (code, name) VALUES
    ('NGO_A', 'NGO A'),
    ('NGO_B', 'NGO B'),
    ('MINISTRY_NIGERIA', 'Federal Ministry of Health');

-- password: password
INSERT INTO app_user (username, password, tenant_id, role) VALUES
    ('chw1', '$2a$10$N9qo8uLOickgx2ZMRZoMyM7M9wS2jLQk0iQF5EYh3YOEoTP0TOZGa', 'NGO_A', 'COMMUNITY_HEALTH_WORKER'),
    ('clinician1', '$2a$10$N9qo8uLOickgx2ZMRZoMyM7M9wS2jLQk0iQF5EYh3YOEoTP0TOZGa', 'NGO_A', 'CLINICIAN'),
    ('manager1', '$2a$10$N9qo8uLOickgx2ZMRZoMyM7M9wS2jLQk0iQF5EYh3YOEoTP0TOZGa', 'NGO_A', 'PROGRAM_MANAGER'),
    ('admin1', '$2a$10$N9qo8uLOickgx2ZMRZoMyM7M9wS2jLQk0iQF5EYh3YOEoTP0TOZGa', 'NGO_A', 'ADMIN');
