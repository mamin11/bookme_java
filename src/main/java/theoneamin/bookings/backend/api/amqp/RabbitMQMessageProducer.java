package theoneamin.bookings.backend.api.amqp;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class RabbitMQMessageProducer {
    public static final String ROUTING_KEY = "notification.queue";
    public static final String STAFF_ROUTING_KEY = "notification.staff.queue";
    private final CachingConnectionFactory cachingConnectionFactory;

    @Bean
    public Queue createBookingNotificationQueue() {
        return new Queue(ROUTING_KEY);
    }

    @Bean
    public Queue createStaffNotificationQueue() {
        return new Queue(STAFF_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(cachingConnectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}
