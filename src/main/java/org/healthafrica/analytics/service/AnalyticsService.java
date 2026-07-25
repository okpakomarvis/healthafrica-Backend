package org.healthafrica.analytics.service;

import lombok.RequiredArgsConstructor;
import org.healthafrica.analytics.dto.AnalyticsSummaryResponse;
import org.healthafrica.analytics.dto.UserRegistrationStats;
import org.healthafrica.auth.entity.Role;
import org.healthafrica.auth.entity.User;
import org.healthafrica.auth.repository.UserRepository;
import org.healthafrica.communityevents.entity.VaccinationEvent;
import org.healthafrica.communityevents.repository.VaccinationRepository;
import org.healthafrica.fhir.entity.FhirPatient;
import org.healthafrica.fhir.repository.FhirPatientRepository;
import org.healthafrica.gis.dto.GeoJsonFeature;
import org.healthafrica.gis.dto.GeoJsonResponse;
import org.healthafrica.shared.tenant.TenantContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final UserRepository userRepository;
    private final FhirPatientRepository patientRepository;
    private final VaccinationRepository vaccinationRepository;

    public AnalyticsSummaryResponse summary() {
        String tenantId = TenantContextHolder.getTenant();
        return new AnalyticsSummaryResponse(
                userRepository.findByTenantId(tenantId).size(),
                patientRepository.countByTenantId(tenantId),
                vaccinationRepository.findByTenantId(tenantId).size());
    }

    public List<UserRegistrationStats> registrationsByUser() {
        String tenantId = TenantContextHolder.getTenant();
        List<User> users = userRepository.findByTenantId(tenantId);
        List<FhirPatient> patients = patientRepository.findByTenantId(tenantId);
        List<VaccinationEvent> vaccinations = vaccinationRepository.findByTenantId(tenantId);

        Map<Long, long[]> counts = new HashMap<>();
        for (User user : users) {
            counts.put(user.getId(), new long[]{0, 0});
        }

        for (FhirPatient patient : patients) {
            if (patient.getCreatedByUserId() != null) {
                counts.computeIfAbsent(patient.getCreatedByUserId(), k -> new long[]{0, 0})[0]++;
            }
        }

        for (VaccinationEvent vaccination : vaccinations) {
            if (vaccination.getRecordedByUserId() != null) {
                counts.computeIfAbsent(vaccination.getRecordedByUserId(), k -> new long[]{0, 0})[1]++;
            }
        }

        List<UserRegistrationStats> result = new ArrayList<>(users.stream()
                .map(user -> {
                    long[] c = counts.getOrDefault(user.getId(), new long[]{0, 0});
                    return new UserRegistrationStats(
                            user.getId(),
                            user.getUsername(),
                            user.getRole(),
                            c[0],
                            c[1]);
                })
                .toList());

        long unassignedPatients = patients.stream()
                .filter(p -> p.getCreatedByUserId() == null).count();
        long unassignedVaccinations = vaccinations.stream()
                .filter(v -> v.getRecordedByUserId() == null).count();
        if (unassignedPatients > 0 || unassignedVaccinations > 0) {
            result.add(new UserRegistrationStats(
                    -1L,
                    "Unassigned",
                    Role.COMMUNITY_HEALTH_WORKER,
                    unassignedPatients,
                    unassignedVaccinations));
        }

        return result;
    }

    public GeoJsonResponse patientMap(Long userId) {
        String tenantId = TenantContextHolder.getTenant();
        List<FhirPatient> patients = userId == null
                ? patientRepository.findByTenantId(tenantId)
                : patientRepository.findByTenantIdAndCreatedByUserId(tenantId, userId);

        List<GeoJsonFeature> features = new ArrayList<>();
        for (FhirPatient patient : patients) {
            if (patient.getLocation() == null) {
                continue;
            }

            Map<String, Object> geometry = Map.of(
                    "type", "Point",
                    "coordinates", List.of(
                            patient.getLocation().getX(),
                            patient.getLocation().getY()));

            Map<String, Object> properties = new HashMap<>();
            properties.put("id", patient.getId());
            properties.put("firstName", patient.getFirstName());
            properties.put("lastName", patient.getLastName());
            properties.put("createdByUserId", patient.getCreatedByUserId());

            features.add(new GeoJsonFeature("Feature", geometry, properties));
        }

        return new GeoJsonResponse("FeatureCollection", features);
    }

    public GeoJsonResponse vaccinationMap(Long userId) {
        String tenantId = TenantContextHolder.getTenant();
        List<VaccinationEvent> vaccinations = vaccinationRepository.findByTenantId(tenantId);

        List<GeoJsonFeature> features = new ArrayList<>();
        for (VaccinationEvent event : vaccinations) {
            if (event.getLocation() == null) {
                continue;
            }
            if (userId != null && !userId.equals(event.getRecordedByUserId())) {
                continue;
            }

            Map<String, Object> geometry = Map.of(
                    "type", "Point",
                    "coordinates", List.of(
                            event.getLocation().getX(),
                            event.getLocation().getY()));

            Map<String, Object> properties = new HashMap<>();
            properties.put("id", event.getId());
            properties.put("vaccineName", event.getVaccineName());
            properties.put("patientId", event.getPatientId());
            properties.put("dateAdministered", event.getDateAdministered());
            properties.put("recordedByUserId", event.getRecordedByUserId());

            features.add(new GeoJsonFeature("Feature", geometry, properties));
        }

        return new GeoJsonResponse("FeatureCollection", features);
    }
}
