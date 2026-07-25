package org.healthafrica.fhir.dto;


import java.time.Instant;

public record ObservationResponse(

        Long id,

        Long patientId,

        String observationType,

        String value,

        Instant timestamp,

        Integer version

) {
}
