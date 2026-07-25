package org.healthafrica.notification.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE =
            "health.events.exchange";

    public static final String EMAIL_QUEUE =
            "notification.email.queue";

    public static final String SMS_QUEUE =
            "notification.sms.queue";

    public static final String PUSH_QUEUE =
            "notification.push.queue";

    @Bean
    public TopicExchange exchange() {

        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue emailQueue() {

        return new Queue(EMAIL_QUEUE);
    }

    @Bean
    public Queue smsQueue() {

        return new Queue(SMS_QUEUE);
    }

    @Bean
    public Queue pushQueue() {

        return new Queue(PUSH_QUEUE);
    }

    @Bean
    public Binding emailBinding() {

        return BindingBuilder
                .bind(emailQueue())
                .to(exchange())
                .with("vaccination.recorded");
    }

    @Bean
    public Binding smsBinding() {

        return BindingBuilder
                .bind(smsQueue())
                .to(exchange())
                .with("vaccination.recorded");
    }

    @Bean
    public Binding pushBinding() {

        return BindingBuilder
                .bind(pushQueue())
                .to(exchange())
                .with("vaccination.recorded");
    }
    @Bean
    public Queue notificationQueue() {

        return new Queue(
                "notification.queue"
        );
    }
}