package org.healthafrica.notification.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.healthafrica.notification.config.RabbitMQConfig;
import org.healthafrica.notification.dto.NotificationMessage;
import org.healthafrica.shared.events.VaccinationRecordedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class NotificationPublisher {

    private final RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper;

    @EventListener
    public void handle(
            VaccinationRecordedEvent event) {

        try {

            NotificationMessage message =
                    new NotificationMessage(
                            event.tenantId(),
                            "VACCINATION_RECORDED",
                            "Vaccination recorded for patient "
                                    + event.patientId(),
                            Instant.now()
                    );

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE,
                    "vaccination.recorded",
                    objectMapper.writeValueAsString(
                            message
                    )
            );

        } catch (Exception ex) {

            throw new RuntimeException(ex);
        }
    }
}