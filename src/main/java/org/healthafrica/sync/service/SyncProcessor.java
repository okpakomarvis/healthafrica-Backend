package org.healthafrica.sync.service;

import lombok.RequiredArgsConstructor;
import org.healthafrica.communityevents.dto.CreateVaccinationRequest;
import org.healthafrica.communityevents.service.VaccinationService;
import org.healthafrica.fhir.dto.CreatePatientRequest;
import org.healthafrica.fhir.entity.FhirPatient;
import org.healthafrica.fhir.repository.FhirPatientRepository;
import org.healthafrica.fhir.service.FhirPatientService;
import org.healthafrica.shared.tenant.TenantContextHolder;
import org.healthafrica.sync.dto.OperationType;
import org.healthafrica.sync.dto.SyncOperation;
import org.healthafrica.sync.exeception.SyncConflictException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Processes individual sync operations from offline mobile clients.
 */
@Component
@RequiredArgsConstructor
public class SyncProcessor {

    private final FhirPatientService patientService;
    private final SyncPatientService syncPatientService;
    private final VaccinationService vaccinationService;
    private final FhirPatientRepository patientRepository;
    private final ConflictDetector conflictDetector;

    public Long process(SyncOperation operation) {
        return switch (operation.operationType()) {
            case CREATE_PATIENT -> processPatient(operation);
            case CREATE_VACCINATION -> processVaccination(operation);
            case UPDATE_PATIENT -> processUpdatePatient(operation);
        };
    }

    private Long processPatient(SyncOperation operation) {
        Map<String, Object> payload = operation.payload();
        String tenantId = TenantContextHolder.getTenant();

        CreatePatientRequest request = new CreatePatientRequest(
                stringValue(payload, "firstName"),
                stringValue(payload, "lastName"),
                stringValue(payload, "gender"),
                LocalDate.parse(stringValue(payload, "birthDate")),
                operation.clientReferenceId(),
                optionalDouble(payload, "latitude"),
                optionalDouble(payload, "longitude"));

        if (operation.clientReferenceId() != null
                && !operation.clientReferenceId().isBlank()) {
            return syncPatientService.createIfNeeded(
                    tenantId,
                    operation.clientReferenceId(),
                    request);
        }

        return patientService.create(request).id();
    }

    private Long processVaccination(SyncOperation operation) {
        Map<String, Object> payload = operation.payload();

        return vaccinationService.create(new CreateVaccinationRequest(
                Long.valueOf(stringValue(payload, "patientId")),
                stringValue(payload, "vaccineName"),
                LocalDate.parse(stringValue(payload, "dateAdministered")),
                Double.valueOf(stringValue(payload, "latitude")),
                Double.valueOf(stringValue(payload, "longitude"))
        )).id();
    }

    private Long processUpdatePatient(SyncOperation operation) {
        String tenantId = TenantContextHolder.getTenant();

        FhirPatient patient = patientRepository
                .findByIdAndTenantId(operation.serverId(), tenantId)
                .orElseThrow(() -> new NoSuchElementException("Patient not found"));

        try {
            conflictDetector.validate(
                    operation.version(),
                    patient.getVersion().longValue());
        } catch (SyncConflictException ex) {
            conflictDetector.recordConflict(
                    tenantId,
                    operation,
                    patient.getVersion().longValue(),
                    ex.getMessage());
            throw ex;
        }

        Map<String, Object> payload = operation.payload();
        patient.setFirstName(stringValue(payload, "firstName"));
        patient.setLastName(stringValue(payload, "lastName"));

        if (payload.containsKey("gender")) {
            patient.setGender(stringValue(payload, "gender"));
        }

        if (payload.containsKey("birthDate")) {
            patient.setBirthDate(LocalDate.parse(stringValue(payload, "birthDate")));
        }

        patientRepository.save(patient);
        return patient.getId();
    }

    private String stringValue(Map<String, Object> payload, String key) {
        Object value = payload.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing payload field: " + key);
        }
        return value.toString();
    }

    private Double optionalDouble(Map<String, Object> payload, String key) {
        Object value = payload.get(key);
        if (value == null) {
            return null;
        }
        return Double.valueOf(value.toString());
    }
}
