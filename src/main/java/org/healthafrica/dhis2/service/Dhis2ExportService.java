package org.healthafrica.dhis2.service;

import lombok.RequiredArgsConstructor;
import org.healthafrica.communityevents.entity.VaccinationEvent;
import org.healthafrica.communityevents.repository.VaccinationRepository;
import org.healthafrica.dhis2.client.MockDhis2Client;
import org.healthafrica.dhis2.dto.*;
import org.healthafrica.dhis2.mapper.Dhis2Mapper;
import org.healthafrica.shared.tenant.TenantContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Exports vaccination data to DHIS2-compatible payloads.
 */
@Service
@RequiredArgsConstructor
public class Dhis2ExportService {

    private final VaccinationRepository repository;
    private final Dhis2Mapper mapper;
    private final MockDhis2Client client;

    public Dhis2ExportResponse export() {
        List<Dhis2EventDto> events = repository
                .findByTenantId(TenantContextHolder.getTenant())
                .stream()
                .map(mapper::map)
                .toList();

        Dhis2Payload payload = new Dhis2Payload(events);
        boolean success = client.export(payload);

        return new Dhis2ExportResponse(success, "Export completed", events.size());
    }

    public Dhis2ExportResponse exportVaccination(VaccinationEvent vaccination) {
        Dhis2Payload payload = new Dhis2Payload(List.of(mapper.map(vaccination)));
        client.export(payload);

        return new Dhis2ExportResponse(true, "Vaccination exported", 1);
    }
}
