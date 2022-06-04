package theoneamin.bookings.backend.api.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import theoneamin.bookings.backend.api.config.BookingEndpoints;
import theoneamin.bookings.backend.api.entity.booking.BookingEntity;
import theoneamin.bookings.backend.api.entity.booking.BookingRequest;
import theoneamin.bookings.backend.api.entity.booking.BookingResponse;
import theoneamin.bookings.backend.api.service.BookingService;

import javax.validation.Valid;
import java.util.List;

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

        //todo: make transactional
        BookingResponse response = bookingService.createBooking(bookingRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(BookingEndpoints.BOOKING_BY_ID)
    public ResponseEntity<BookingEntity> getBooking(@PathVariable String id) {
        log.info("{} request: {}", BookingEndpoints.BOOKING_BY_ID, id);
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getBookingById(id));
    }

    @GetMapping(BookingEndpoints.BOOKING_ALL)
    public ResponseEntity<List<BookingEntity>> getAllBooking() {
        log.info("{} request", BookingEndpoints.BOOKING_ALL);
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getAllBookings());
    }

    @GetMapping(BookingEndpoints.BOOKING_BY_STAFF)
    public ResponseEntity<List<BookingEntity>> getBookingsByStaff(@PathVariable Integer id) {
        log.info("{} request: {}", BookingEndpoints.BOOKING_BY_STAFF, id);
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getByStaffId(id));
    }

    @GetMapping(BookingEndpoints.BOOKING_BY_CUSTOMER)
    public ResponseEntity<List<BookingEntity>> getBookingsByCustomer(@PathVariable Integer id) {
        log.info("{} request: {}", BookingEndpoints.BOOKING_BY_CUSTOMER, id);
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getByCustomerId(id));
    }

    @GetMapping(BookingEndpoints.BOOKING_BY_SERVICE)
    public ResponseEntity<List<BookingEntity>> getBookingsByService(@PathVariable Integer id) {
        log.info("{} request: {}", BookingEndpoints.BOOKING_BY_SERVICE, id);
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getByServiceId(id));
    }

    @GetMapping(BookingEndpoints.BOOKING_BY_DATE)
    public ResponseEntity<List<BookingEntity>> getBookingsByDate(@PathVariable String date) {
        log.info("{} request: {}", BookingEndpoints.BOOKING_BY_DATE, date);
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getByDate(date));
    }
}
