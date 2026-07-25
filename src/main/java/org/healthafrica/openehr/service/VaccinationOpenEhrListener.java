package org.healthafrica.openehr.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.healthafrica.openehr.entity.Composition;
import org.healthafrica.openehr.mapper.OpenEhrMapper;
import org.healthafrica.openehr.repository.CompositionRepository;
import org.healthafrica.shared.events.VaccinationRecordedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class VaccinationOpenEhrListener {

    private final CompositionRepository repository;
    private final OpenEhrMapper mapper;

    @EventListener
    public void handle(
            VaccinationRecordedEvent event) {

        Composition composition =
                new Composition();

        composition.setTenantId(
                event.tenantId());

        composition.setPatientId(
                event.patientId());

        composition.setArchetypeId(
                "openEHR-EHR-OBSERVATION.immunisation.v1");

        composition.setTimestamp(
                Instant.now());

        composition.setContent(
                mapper.buildVaccinationComposition(
                        event.patientId(),
                        event.vaccineName()
                )
        );

        repository.save(composition);

        log.info(
                "OpenEHR composition created. patientId={}",
                event.patientId()
        );
    }
}
