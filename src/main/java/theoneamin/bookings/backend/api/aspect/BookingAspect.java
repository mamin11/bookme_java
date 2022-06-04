package theoneamin.bookings.backend.api.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import theoneamin.bookings.backend.api.amqp.RabbitMQMessageProducer;
import theoneamin.bookings.backend.api.entity.booking.BookingResponse;

import static theoneamin.bookings.backend.api.amqp.RabbitMQMessageProducer.ROUTING_KEY;
import static theoneamin.bookings.backend.api.amqp.RabbitMQMessageProducer.STAFF_ROUTING_KEY;

@Aspect
@Component
@Slf4j
public class BookingAspect {
    @Autowired RabbitTemplate rabbitTemplate;

    @AfterReturning(value="execution(* theoneamin.bookings.backend.api.service.BookingService.createBooking(*))", returning="bookingResponse")
    public void afterReturningAdvice(JoinPoint joinPoint, BookingResponse bookingResponse)
    {
        log.info("After Returning method: "+joinPoint.getSignature());
        log.info(bookingResponse.getBooking().toString());
        if (bookingResponse.getBooking().isNotifyCustomer()) {
            rabbitTemplate.convertAndSend(ROUTING_KEY, bookingResponse.getBooking());
            log.info("Published message to: " + ROUTING_KEY);
        } else {
            log.info("Notify_customer: {}", bookingResponse.getBooking().isNotifyCustomer());
            rabbitTemplate.convertAndSend(STAFF_ROUTING_KEY, bookingResponse.getBooking());
            log.info("Published message to: " + STAFF_ROUTING_KEY);
        }
    }
}
