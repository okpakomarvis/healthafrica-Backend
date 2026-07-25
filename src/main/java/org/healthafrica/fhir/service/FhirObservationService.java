package org.healthafrica.fhir.service;

import lombok.RequiredArgsConstructor;
import org.healthafrica.fhir.dto.*;
import org.healthafrica.fhir.entity.FhirObservation;
import org.healthafrica.fhir.mapper.FhirResourceMapper;
import org.healthafrica.fhir.repository.FhirObservationRepository;
import org.healthafrica.shared.tenant.TenantContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.NoSuchElementException;

/**
 * Service for FHIR Observation resource lifecycle operations.
 */
@Service
@RequiredArgsConstructor
public class FhirObservationService {

    private final FhirObservationRepository repository;
    private final FhirResourceMapper mapper;

    public ObservationResponse create(CreateObservationRequest request) {
        FhirObservation observation = new FhirObservation();
        observation.setTenantId(TenantContextHolder.getTenant());
        observation.setPatientId(request.patientId());
        observation.setObservationType(request.observationType());
        observation.setValue(request.value());
        observation.setTimestamp(Instant.now());
        observation.setResourceJson("{}");

        repository.save(observation);

        observation.setResourceJson(mapper.buildObservationJson(observation));
        repository.save(observation);

        return map(observation);
    }

    public ObservationResponse get(Long id) {
        FhirObservation observation = repository
                .findByIdAndTenantId(id, TenantContextHolder.getTenant())
                .orElseThrow(() -> new NoSuchElementException("Observation not found"));

        return map(observation);
    }

    private ObservationResponse map(FhirObservation observation) {
        return new ObservationResponse(
                observation.getId(),
                observation.getPatientId(),
                observation.getObservationType(),
                observation.getValue(),
                observation.getTimestamp(),
                observation.getVersion());
    }
}
