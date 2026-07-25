package org.healthafrica.communityevents.service;

import lombok.RequiredArgsConstructor;
import org.healthafrica.auth.security.SecurityUtils;
import org.healthafrica.communityevents.dto.CreateVaccinationRequest;
import org.healthafrica.communityevents.dto.VaccinationResponse;
import org.healthafrica.communityevents.entity.VaccinationEvent;
import org.healthafrica.communityevents.repository.VaccinationRepository;
import org.healthafrica.shared.events.VaccinationRecordedEvent;
import org.healthafrica.shared.tenant.TenantContextHolder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service for recording and querying community vaccination events.
 */
@Service
@RequiredArgsConstructor
public class VaccinationService {

    private final VaccinationRepository repository;
    private final GeometryFactory geometryFactory;
    private final ApplicationEventPublisher publisher;

    public VaccinationResponse create(CreateVaccinationRequest request) {
        VaccinationEvent vaccination = new VaccinationEvent();
        vaccination.setTenantId(TenantContextHolder.getTenant());
        vaccination.setPatientId(request.patientId());
        vaccination.setVaccineName(request.vaccineName());
        vaccination.setDateAdministered(request.dateAdministered());

        Point point = geometryFactory.createPoint(
                new Coordinate(request.longitude(), request.latitude()));
        point.setSRID(4326);
        vaccination.setLocation(point);
        vaccination.setRecordedByUserId(SecurityUtils.currentUserIdLong());

        repository.save(vaccination);

        publisher.publishEvent(new VaccinationRecordedEvent(
                vaccination.getId(),
                vaccination.getPatientId(),
                vaccination.getVaccineName(),
                vaccination.getTenantId(),
                SecurityUtils.currentUserId()));

        return map(vaccination, request.latitude(), request.longitude());
    }

    public List<VaccinationResponse> findAll() {
        return repository.findByTenantId(TenantContextHolder.getTenant()).stream()
                .map(event -> map(
                        event,
                        event.getLocation() != null ? event.getLocation().getY() : null,
                        event.getLocation() != null ? event.getLocation().getX() : null))
                .toList();
    }

    public VaccinationResponse get(Long id) {
        VaccinationEvent event = repository
                .findByIdAndTenantId(id, TenantContextHolder.getTenant())
                .orElseThrow(() -> new NoSuchElementException("Vaccination not found"));

        return map(
                event,
                event.getLocation() != null ? event.getLocation().getY() : null,
                event.getLocation() != null ? event.getLocation().getX() : null);
    }

    private VaccinationResponse map(
            VaccinationEvent vaccination,
            Double latitude,
            Double longitude) {

        return new VaccinationResponse(
                vaccination.getId(),
                vaccination.getPatientId(),
                vaccination.getVaccineName(),
                vaccination.getDateAdministered(),
                latitude,
                longitude,
                vaccination.getVersion());
    }
}
