package org.healthafrica.dhis2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.healthafrica.communityevents.entity.VaccinationEvent;
import org.healthafrica.communityevents.repository.VaccinationRepository;
import org.healthafrica.shared.events.VaccinationRecordedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VaccinationDhis2Listener {

    private final VaccinationRepository repository;

    private final Dhis2ExportService exportService;

    @EventListener
    public void handle(
            VaccinationRecordedEvent event) {

        VaccinationEvent vaccination =
                repository.findById(
                                event.vaccinationId()
                        )
                        .orElseThrow();

        exportService.exportVaccination(
                vaccination
        );

        log.info(
                "Vaccination exported to DHIS2. id={}",
                vaccination.getId()
        );
    }
}
