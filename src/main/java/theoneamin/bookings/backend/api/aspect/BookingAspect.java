package theoneamin.bookings.backend.api.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import theoneamin.bookings.backend.api.entity.booking.BookingResponse;

@Aspect
@Component
@Slf4j
public class BookingAspect {
//    @AfterReturning(value="execution(* theoneamin.bookings.backend.api.service.BookingService.*(..))", returning="bookingResponse")
    @AfterReturning(value="execution(* theoneamin.bookings.backend.api.service.BookingService.createBooking(*))", returning="bookingResponse")
    public void afterReturningAdvice(JoinPoint joinPoint, BookingResponse bookingResponse)
    {
        log.info("After Returning method: "+joinPoint.getSignature());
        log.info(bookingResponse.getBooking().toString());
    }
}
