package org.healthafrica.openehr.mapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OpenEhrMapper {

    private final ObjectMapper objectMapper;

    public String buildVaccinationComposition(
            Long patientId,
            String vaccineName) {

        try {

            Map<String, Object> composition =
                    new HashMap<>();

            composition.put(
                    "archetypeId",
                    "openEHR-EHR-OBSERVATION.immunisation.v1");

            composition.put(
                    "patientId",
                    patientId);

            composition.put(
                    "vaccineName",
                    vaccineName);

            composition.put(
                    "timestamp",
                    Instant.now());

            composition.put(
                    "clinicalDomain",
                    "IMMUNIZATION");

            return objectMapper.writeValueAsString(
                    composition);

        } catch (Exception ex) {

            throw new RuntimeException(ex);
        }
    }
}