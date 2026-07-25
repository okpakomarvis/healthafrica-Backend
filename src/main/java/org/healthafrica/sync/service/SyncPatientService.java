package org.healthafrica.sync.service;

import lombok.RequiredArgsConstructor;
import org.healthafrica.auth.security.SecurityUtils;
import org.healthafrica.fhir.dto.CreatePatientRequest;
import org.healthafrica.fhir.entity.FhirPatient;
import org.healthafrica.fhir.mapper.FhirResourceMapper;
import org.healthafrica.fhir.repository.FhirPatientRepository;
import org.healthafrica.shared.events.PatientCreatedEvent;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Idempotent patient creation for offline sync using client reference identifiers.
 */
@Service
@RequiredArgsConstructor
public class SyncPatientService {

    private final FhirPatientRepository repository;
    private final FhirResourceMapper mapper;
    private final GeometryFactory geometryFactory;
    private final ApplicationEventPublisher publisher;

    public Long createIfNeeded(
            String tenantId,
            String clientReferenceId,
            CreatePatientRequest request) {

        return repository
                .findByTenantIdAndClientReferenceId(tenantId, clientReferenceId)
                .map(FhirPatient::getId)
                .orElseGet(() -> {
                    FhirPatient patient = new FhirPatient();
                    patient.setTenantId(tenantId);
                    patient.setClientReferenceId(clientReferenceId);
                    patient.setFirstName(request.firstName());
                    patient.setLastName(request.lastName());
                    patient.setGender(request.gender());
                    patient.setBirthDate(request.birthDate());
                    patient.setCreatedByUserId(SecurityUtils.currentUserIdLong());
                    if (request.latitude() != null && request.longitude() != null) {
                        Point point = geometryFactory.createPoint(
                                new Coordinate(request.longitude(), request.latitude()));
                        point.setSRID(4326);
                        patient.setLocation(point);
                    }
                    patient.setResourceJson("{}");
                    repository.save(patient);
                    patient.setResourceJson(mapper.buildPatientJson(patient));
                    repository.save(patient);
                    publisher.publishEvent(new PatientCreatedEvent(
                            patient.getId(),
                            patient.getTenantId(),
                            SecurityUtils.currentUserId()));
                    return patient.getId();
                });
    }
}
