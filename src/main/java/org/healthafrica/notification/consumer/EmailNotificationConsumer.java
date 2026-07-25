package org.healthafrica.notification.consumer;


import lombok.extern.slf4j.Slf4j;
import org.healthafrica.notification.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailNotificationConsumer {

    @RabbitListener(
            queues = RabbitMQConfig.EMAIL_QUEUE
    )
    public void consume(String payload) {

        log.info(
                "EMAIL notification processed {}",
                payload
        );
    }
}
