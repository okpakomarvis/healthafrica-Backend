package org.healthafrica.fhir.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.healthafrica.fhir.entity.FhirObservation;
import org.healthafrica.fhir.entity.FhirPatient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FhirResourceMapper {

    private final ObjectMapper objectMapper;

    public String buildPatientJson(
            FhirPatient patient) {

        try {

            Map<String,Object> resource =
                    new HashMap<>();

            resource.put(
                    "resourceType",
                    "Patient");

            resource.put(
                    "id",
                    patient.getId());

            resource.put(
                    "firstName",
                    patient.getFirstName());

            resource.put(
                    "lastName",
                    patient.getLastName());

            resource.put(
                    "gender",
                    patient.getGender());

            resource.put(
                    "birthDate",
                    patient.getBirthDate());

            return objectMapper
                    .writeValueAsString(resource);

        } catch (Exception ex) {

            throw new RuntimeException(ex);
        }
    }

    public String buildObservationJson(
            FhirObservation observation) {

        try {

            Map<String,Object> resource =
                    new HashMap<>();

            resource.put(
                    "resourceType",
                    "Observation");

            resource.put(
                    "id",
                    observation.getId());

            resource.put(
                    "patientId",
                    observation.getPatientId());

            resource.put(
                    "observationType",
                    observation.getObservationType());

            resource.put(
                    "value",
                    observation.getValue());

            resource.put(
                    "timestamp",
                    observation.getTimestamp());

            return objectMapper
                    .writeValueAsString(resource);

        } catch (Exception ex) {

            throw new RuntimeException(ex);
        }
    }
}
