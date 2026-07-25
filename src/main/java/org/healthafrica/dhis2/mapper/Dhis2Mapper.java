package org.healthafrica.dhis2.mapper;

import org.healthafrica.communityevents.entity.VaccinationEvent;
import org.healthafrica.dhis2.dto.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Dhis2Mapper {

    public Dhis2EventDto map(
            VaccinationEvent vaccination) {

        return new Dhis2EventDto(

                "IMMUNIZATION_PROGRAM",

                "LAGOS_MAINLAND",

                vaccination
                        .getDateAdministered()
                        .toString(),

                List.of(

                        new DataValueDto(
                                "PATIENT_ID",
                                vaccination
                                        .getPatientId()
                                        .toString()
                        ),

                        new DataValueDto(
                                "VACCINE_NAME",
                                vaccination
                                        .getVaccineName()
                        )
                )
        );
    }
}
