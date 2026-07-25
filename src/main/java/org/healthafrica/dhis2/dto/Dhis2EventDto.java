package org.healthafrica.dhis2.dto;

import java.util.List;

public record Dhis2EventDto(

        String program,

        String orgUnit,

        String eventDate,

        List<DataValueDto> dataValues

) {
}
