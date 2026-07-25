package org.healthafrica.fhir.service;

import lombok.RequiredArgsConstructor;
import org.healthafrica.auth.entity.User;
import org.healthafrica.auth.repository.UserRepository;
import org.healthafrica.auth.security.SecurityUtils;
import org.healthafrica.fhir.dto.*;
import org.healthafrica.fhir.entity.FhirPatient;
import org.healthafrica.fhir.mapper.FhirResourceMapper;
import org.healthafrica.fhir.repository.FhirPatientRepository;
import org.healthafrica.shared.events.PatientCreatedEvent;
import org.healthafrica.shared.tenant.TenantContextHolder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FhirPatientService {

    private final FhirPatientRepository repository;
    private final FhirResourceMapper mapper;
    private final ApplicationEventPublisher publisher;
    private final UserRepository userRepository;
    private final GeometryFactory geometryFactory;

    public PatientResponse create(CreatePatientRequest request) {
        String tenantId = TenantContextHolder.getTenant();

        String clientRef = request.clientReferenceId() != null
                && !request.clientReferenceId().isBlank()
                ? request.clientReferenceId()
                : UUID.randomUUID().toString();

        FhirPatient patient = repository
                .findByTenantIdAndClientReferenceId(tenantId, clientRef)
                .orElseGet(FhirPatient::new);

        patient.setTenantId(tenantId);
        patient.setClientReferenceId(clientRef);
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

        if (patient.getResourceJson() == null) {
            patient.setResourceJson("{}");
        }

        repository.save(patient);

        patient.setResourceJson(mapper.buildPatientJson(patient));
        repository.save(patient);

        publisher.publishEvent(new PatientCreatedEvent(
                patient.getId(),
                patient.getTenantId(),
                SecurityUtils.currentUserId()));

        return map(patient);
    }

    public List<PatientResponse> list() {
        return repository.findByTenantId(TenantContextHolder.getTenant()).stream()
                .map(this::map)
                .toList();
    }

    public PatientResponse get(Long id) {
        FhirPatient patient = repository
                .findByIdAndTenantId(id, TenantContextHolder.getTenant())
                .orElseThrow(() -> new NoSuchElementException("Patient not found"));

        return map(patient);
    }

    public PatientResponse update(Long id, UpdatePatientRequest request) {
        FhirPatient patient = repository
                .findByIdAndTenantId(id, TenantContextHolder.getTenant())
                .orElseThrow(() -> new NoSuchElementException("Patient not found"));

        patient.setFirstName(request.firstName());
        patient.setLastName(request.lastName());
        patient.setGender(request.gender());
        patient.setBirthDate(request.birthDate());
        patient.setResourceJson(mapper.buildPatientJson(patient));

        repository.save(patient);

        return map(patient);
    }

    private PatientResponse map(FhirPatient patient) {
        String createdByUsername = null;
        if (patient.getCreatedByUserId() != null) {
            createdByUsername = userRepository.findById(patient.getCreatedByUserId())
                    .map(User::getUsername)
                    .orElse(null);
        }

        Double latitude = null;
        Double longitude = null;
        if (patient.getLocation() != null) {
            latitude = patient.getLocation().getY();
            longitude = patient.getLocation().getX();
        }

        return new PatientResponse(
                patient.getId(),
                patient.getTenantId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getGender(),
                patient.getBirthDate(),
                patient.getVersion(),
                patient.getClientReferenceId(),
                patient.getCreatedByUserId(),
                createdByUsername,
                latitude,
                longitude);
    }
}
