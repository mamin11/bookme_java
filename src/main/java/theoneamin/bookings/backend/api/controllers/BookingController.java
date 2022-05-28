package theoneamin.bookings.backend.api.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import theoneamin.bookings.backend.api.config.BookingEndpoints;
import theoneamin.bookings.backend.api.entity.booking.BookingDTO;
import theoneamin.bookings.backend.api.entity.booking.BookingEntity;
import theoneamin.bookings.backend.api.entity.booking.BookingRequest;
import theoneamin.bookings.backend.api.entity.booking.BookingResponse;
import theoneamin.bookings.backend.api.repository.BookingRepository;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping()
@CrossOrigin(origins = "*")
@Slf4j
@Validated
public class BookingController {

    @Autowired
    BookingRepository bookingRepository;

    @GetMapping(BookingEndpoints.TEST)
    public ResponseEntity<String> all() {
        return ResponseEntity.status(200).body("hello there");
    }

    @PostMapping(BookingEndpoints.BOOKING_ADD)
    public ResponseEntity<BookingResponse> addBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        log.info("{} request: {}", BookingEndpoints.BOOKING_ADD, bookingRequest);

        BookingEntity booking = new BookingEntity();
        booking.setCustomerId(bookingRequest.getCustomerId());
        booking.setStaffId(bookingRequest.getStaffId());
        booking.setServiceId(bookingRequest.getServiceId());
        booking.setBookingDate(bookingRequest.getBookingDate());
        booking.setBookingSlots(bookingRequest.getBookingSlots());
        booking.setNotifyCustomer(bookingRequest.isNotifyCustomer());

        BookingEntity persisted = bookingRepository.save(booking);

        BookingResponse response = BookingResponse.builder()
                .message("Successfully created booking")
                .booking(BookingDTO.builder()
                        .bookingId(persisted.getId())
                        .customerEmail("test@test.com")
                        .date(LocalDate.now())
                        .timeSlots(booking.getBookingSlots())
                        .build())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
