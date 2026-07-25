package org.healthafrica.communityevents.controller;

import lombok.RequiredArgsConstructor;
import org.healthafrica.fhir.dto.CreateObservationRequest;
import org.healthafrica.fhir.service.FhirObservationService;
import org.healthafrica.shared.events.VaccinationRecordedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VaccinationFhirListener {

    private final FhirObservationService
            observationService;

    @EventListener
    public void handle(
            VaccinationRecordedEvent event) {

        observationService.create(
                new CreateObservationRequest(
                        event.patientId(),
                        "VACCINATION",
                        event.vaccineName()
                )
        );
    }
}
