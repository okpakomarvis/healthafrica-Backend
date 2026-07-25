package org.healthafrica.gis.service;

import lombok.RequiredArgsConstructor;
import org.healthafrica.communityevents.entity.VaccinationEvent;
import org.healthafrica.communityevents.repository.VaccinationRepository;
import org.healthafrica.gis.dto.*;
import org.healthafrica.gis.entity.Clinic;
import org.healthafrica.gis.repository.ClinicRepository;
import org.healthafrica.shared.tenant.TenantContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GIS service for vaccination maps and clinic proximity queries.
 */
@Service
@RequiredArgsConstructor
public class GisService {

    private final VaccinationRepository vaccinationRepository;
    private final ClinicRepository clinicRepository;

    public GeoJsonResponse vaccinationMap() {
        List<VaccinationEvent> vaccinations =
                vaccinationRepository.findByTenantId(TenantContextHolder.getTenant());

        List<GeoJsonFeature> features = new ArrayList<>();

        for (VaccinationEvent event : vaccinations) {
            if (event.getLocation() == null) {
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

    public List<ClinicResponse> nearbyClinics(
            double latitude,
            double longitude,
            double radiusMeters) {

        return clinicRepository.findNearby(
                        TenantContextHolder.getTenant(),
                        latitude,
                        longitude,
                        radiusMeters)
                .stream()
                .map(this::mapClinic)
                .toList();
    }

    private ClinicResponse mapClinic(Clinic clinic) {
        return new ClinicResponse(
                clinic.getId(),
                clinic.getName(),
                clinic.getLocation().getY(),
                clinic.getLocation().getX());
    }
}
