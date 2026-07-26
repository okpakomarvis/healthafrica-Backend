# Public Health Engagement & Data Portal (HealthAfrica)

A modular monolith for community health data capture, FHIR interoperability, openEHR clinical documentation, offline sync, GIS analytics, DHIS2 export, and audit logging.

## Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                     Spring Boot Modular Monolith                │
├──────────┬──────────┬──────────┬──────────┬──────────┬─────────┤
│   auth   │  tenant  │   fhir   │ openehr  │ community│   gis   │
├──────────┴──────────┴──────────┴──────────┴──────────┴─────────┤
│   sync   │ notification │ dhis2 │ audit │ shared (events)      │
└─────────────────────────────────────────────────────────────────┘
         │                    │                    │
    PostgreSQL            RabbitMQ            PostGIS
    + JSONB               (notifications)     (GIS queries)
```

### Modules

| Package | Responsibility |
|---------|----------------|
| `org.healthafrica.auth` | JWT login, role-based access |
| `org.healthafrica.shared.tenant` | Multi-tenancy via `X-Tenant-ID` |
| `org.healthafrica.fhir` | FHIR Patient & Observation resources (JSONB) |
| `org.healthafrica.openehr` | openEHR Composition storage |
| `org.healthafrica.communityevents` | Vaccination events & GIS coordinates |
| `org.healthafrica.sync` | Offline sync with optimistic locking |
| `org.healthafrica.gis` | GeoJSON maps & nearby clinic search |
| `org.healthafrica.notification` | RabbitMQ notification publishing |
| `org.healthafrica.dhis2` | DHIS2 payload export (mock client) |
| `org.healthafrica.audit` | Immutable audit trail |

## Technology Stack

- Java 21, Spring Boot 3.4, Spring Security, Spring Data JPA
- PostgreSQL + PostGIS, Flyway, RabbitMQ
- JJWT, JTS, Hibernate Spatial

## Local Setup

### Prerequisites

- Java 21
- Maven 3.9+
- Docker & Docker Compose

### Start infrastructure

```bash
docker compose up -d
```

This starts:

- **PostgreSQL/PostGIS** on `localhost:5432` (db: `healthdb`, user/pass: `health`)
- **RabbitMQ** on `localhost:5672` (management UI: `http://localhost:15672`)

### Run the application

```bash
cd backend
mvn spring-boot:run
```

Flyway migrations run automatically on startup.

### Seed users

| Username | Password | Role | Tenant |
|----------|----------|------|--------|
| `chw1` | `password` | COMMUNITY_HEALTH_WORKER | NGO_A |
| `clinician1` | `password` | CLINICIAN | NGO_A |
| `manager1` | `password` | PROGRAM_MANAGER | NGO_A |
| `admin1` | `password` | ADMIN | NGO_A |

## API Examples

All requests (except actuator health) require:

```http
X-Tenant-ID: NGO_A
Authorization: Bearer <jwt-token>
```

### Login

```bash
curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -H "X-Tenant-ID: NGO_A" \
  -d '{"username":"chw1","password":"password"}'
```

### Create FHIR Patient (CLINICIAN+)

```bash
TOKEN=<jwt>
curl -s -X POST http://localhost:8080/api/fhir/patients \
  -H "Authorization: Bearer $TOKEN" \
  -H "X-Tenant-ID: NGO_A" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Ada",
    "lastName": "Okafor",
    "gender": "female",
    "birthDate": "2020-05-12",
    "clientReferenceId": "client-patient-001"
  }'
```

### Record Vaccination (CHW+)

```bash
curl -s -X POST http://localhost:8080/api/events/vaccinations \
  -H "Authorization: Bearer $TOKEN" \
  -H "X-Tenant-ID: NGO_A" \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": 1,
    "vaccineName": "BCG",
    "dateAdministered": "2026-06-28",
    "latitude": 6.5244,
    "longitude": 3.3792
  }'
```

### Vaccination GeoJSON Map

```bash
curl -s http://localhost:8080/api/gis/vaccination-map \
  -H "Authorization: Bearer $TOKEN" \
  -H "X-Tenant-ID: NGO_A"
```

### Nearby Clinics (ST_DWithin)

```bash
curl -s "http://localhost:8080/api/gis/nearby-clinics?latitude=6.5244&longitude=3.3792&radiusMeters=10000" \
  -H "Authorization: Bearer $TOKEN" \
  -H "X-Tenant-ID: NGO_A"
```

### Offline Sync

```bash
curl -s -X POST http://localhost:8080/api/sync \
  -H "Authorization: Bearer $TOKEN" \
  -H "X-Tenant-ID: NGO_A" \
  -H "Content-Type: application/json" \
  -d '{
    "operations": [
      {
        "localId": "local-1",
        "clientReferenceId": "client-patient-002",
        "parentLocalId": null,
        "serverId": null,
        "version": null,
        "operationType": "CREATE_PATIENT",
        "payload": {
          "firstName": "Chidi",
          "lastName": "Eze",
          "gender": "male",
          "birthDate": "2019-03-15"
        }
      }
    ]
  }'
```

### DHIS2 Export

```bash
curl -s -X POST http://localhost:8080/api/integration/dhis2/export \
  -H "Authorization: Bearer $TOKEN" \
  -H "X-Tenant-ID: NGO_A"
```

### Audit Log

```bash
curl -s http://localhost:8080/api/audit \
  -H "Authorization: Bearer $TOKEN" \
  -H "X-Tenant-ID: NGO_A"
```

## Event Flow

### Vaccination Recorded

```
POST /api/events/vaccinations
  → VaccinationRecordedEvent (Spring Application Event)
      ├── VaccinationFhirListener      → FHIR Observation
      ├── VaccinationOpenEhrListener    → openEHR Composition
      ├── VaccinationDhis2Listener      → DHIS2 export (mock)
      ├── NotificationPublisher         → RabbitMQ
      │     ├── EmailNotificationConsumer (log)
      │     └── SmsNotificationConsumer (log)
      └── AuditEventListener            → audit_log
```

### Patient Created

```
POST /api/fhir/patients
  → PatientCreatedEvent
      └── AuditEventListener → audit_log
```

### Sync Completed

```
POST /api/sync
  → SyncCompletedEvent
      └── AuditEventListener → audit_log
```

## FHIR Flow

1. Patient created via REST or sync → stored in `fhir_patient` with JSONB `resource_json`
2. Optimistic locking via JPA `@Version`
3. Vaccination triggers automatic FHIR Observation creation
4. Observations stored in `fhir_observation` with JSONB payload

## openEHR Flow

1. Vaccination event triggers `VaccinationOpenEhrListener`
2. Composition built with archetype `openEHR-EHR-OBSERVATION.immunisation.v1`
3. Content stored as JSONB in `openehr_composition`
4. Manual compositions via `POST /api/openehr/compositions`

## Sync Flow

1. Mobile client sends batch of operations to `POST /api/sync`
2. Each operation processed independently
3. `CREATE_PATIENT` uses client reference idempotency
4. `UPDATE_PATIENT` checks version via `ConflictDetector`
5. Conflicts return `CONFLICT` status and are persisted in `sync_conflict`
6. Results array contains per-operation `SUCCESS`, `CONFLICT`, or `FAILED`

## Security

- JWT claims: `userId`, `tenantId`, `role`
- Roles: `COMMUNITY_HEALTH_WORKER`, `CLINICIAN`, `PROGRAM_MANAGER`, `ADMIN`
- `TenantFilter` validates `X-Tenant-ID` matches JWT tenant claim

## Observability

- Actuator: `/actuator/health`, `/actuator/info`, `/actuator/metrics`
- Structured JSON logging to console

## Testing

```bash
cd backend
mvn test
```

- **Unit tests**: JWT service, conflict detector
- **Controller tests**: Auth login
- **Integration tests**: Full context with Testcontainers (PostGIS + RabbitMQ)

## Configuration profiles

| Profile | File | Secrets | When |
|---------|------|---------|------|
| `local` (default) | `application-local.yml` (gitignored) | Local only | `docker compose up -d` + `mvn spring-boot:run` |
| `staging` | `application-staging.yml` | Via env / `.env.staging` only | Neon + CloudAMQP + Render / Docker |

```bash
cp backend/src/main/resources/application-local.yml.example \
   backend/src/main/resources/application-local.yml

cp .env.staging.example .env.staging
# fill Neon JDBC URL, CloudAMQP amqps URL, JWT_SECRET
```

## Docker & staging deploy

```bash
# Local infra
docker compose up -d

# Staging API container (reads .env.staging — never commit it)
docker compose -f docker-compose.staging.yml up -d --build
```

**Render:** use `render.yaml`. Set Neon / CloudAMQP / `JWT_SECRET` in the Render dashboard (`sync: false` vars). Do not put secrets in YAML.

**CI/CD (GitHub Actions):**
- `.github/workflows/ci.yml` — tests on push/PR
- `.github/workflows/deploy-staging.yml` — after tests, triggers Render Deploy Hook

Add GitHub secret `RENDER_DEPLOY_HOOK` from Render → Service → Settings → Deploy Hook.

## Project Structure

```
backend/
├── Dockerfile
├── src/main/java/org/healthafrica/
│   ├── auth/ audit/ communityevents/ dhis2/ fhir/
│   ├── gis/ notification/ openehr/ shared/ sync/ tenant/
├── src/main/resources/
│   ├── application.yml
│   ├── application-staging.yml
│   ├── application-local.yml.example
│   └── db/migration/
└── pom.xml
docker-compose.yml
docker-compose.staging.yml
render.yaml
.env.*.example
.github/workflows/
```
