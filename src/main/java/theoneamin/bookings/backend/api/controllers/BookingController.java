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
import theoneamin.bookings.backend.api.service.BookingService;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping()
@CrossOrigin(origins = "*")
@Slf4j
@Validated
public class BookingController {

    @Autowired BookingService bookingService;

    @GetMapping(BookingEndpoints.TEST)
    public ResponseEntity<String> all() {
        return ResponseEntity.status(200).body("hello there");
    }

    @PostMapping(BookingEndpoints.BOOKING_ADD)
    public ResponseEntity<BookingResponse> addBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        log.info("{} request: {}", BookingEndpoints.BOOKING_ADD, bookingRequest);

        BookingResponse response = bookingService.createBooking(bookingRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
