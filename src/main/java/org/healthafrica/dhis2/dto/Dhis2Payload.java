package org.healthafrica.dhis2.dto;

import java.util.List;

public record Dhis2Payload(

        List<Dhis2EventDto> events

) {
}
