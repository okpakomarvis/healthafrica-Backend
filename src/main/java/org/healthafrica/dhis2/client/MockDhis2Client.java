package org.healthafrica.dhis2.client;

import lombok.extern.slf4j.Slf4j;
import org.healthafrica.dhis2.dto.Dhis2Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MockDhis2Client {

    public boolean export(
            Dhis2Payload payload) {

        log.info(
                "DHIS2 EXPORT PAYLOAD {}",
                payload
        );

        return true;
    }
}
