package theoneamin.bookings.backend.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import theoneamin.bookings.backend.api.entity.booking.*;
import theoneamin.bookings.backend.api.entity.user.UserEntity;
import theoneamin.bookings.backend.api.enums.UserType;
import theoneamin.bookings.backend.api.exception.ApiException;
import theoneamin.bookings.backend.api.repository.BookingRepository;
import theoneamin.bookings.backend.api.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookingService {
    @Autowired private final BookingRepository bookingRepository;
    @Autowired private final UserRepository userRepository;
    @Autowired private final MongoTemplate mongoTemplate;

    public BookingService(BookingRepository bookingRepository, UserRepository userRepository, MongoTemplate mongoTemplate) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Create a booking
     * @param bookingRequest booking request
     * @return booking response
     */
    public BookingResponse createBooking(BookingRequest bookingRequest) {
        // validate customer
        UserEntity customer = userRepository
                .findByEmailAndUserType(bookingRequest.getCustomerEmail(), UserType.CUSTOMER)
                .orElseThrow(() -> new ApiException("Customer does not exist"));

        // validate staff
        UserEntity staff = userRepository
                .findByEmailAndUserType(bookingRequest.getStaffEmail(), UserType.STAFF)
                .orElseThrow(() -> new ApiException("Staff does not exist"));

        // get available slots
        Criteria criteria = Criteria.where("booking_date").is(bookingRequest.getBookingDate())
                .andOperator(Criteria.where("seller_id").is(staff.getId()));
        BookingSlots bookingSlot = mongoTemplate.findOne(Query.query(criteria), BookingSlots.class);
        log.debug("Found booking slots: {}", bookingSlot);

        // validate date
        if (bookingSlot == null) {
            log.error("No booking slots available for date: {}", bookingRequest.getBookingDate());
            throw new ApiException("No booking slots available for date: "+bookingRequest.getBookingDate());
        }

        // validate booking time slot
        boolean isTimeslotValid = isTimeslotValid(bookingRequest.getBookingSlots(), bookingSlot);
        log.info("isTimeSlotValid: {}", isTimeslotValid);
        if (!isTimeslotValid) {
            log.error("Timeslot validation failed: {}, {}", bookingRequest.getBookingSlots(), bookingRequest.getBookingDate());
            throw new ApiException("One or more of selected timeslots is already taken: "+bookingRequest.getBookingSlots());
        }

         // return booking metadata and send message to rabbitMQ

        // remove selected bookings from timeslots for the day
        bookingSlot.setTimeSlots(bookingSlot
                .getTimeSlots()
                .stream()
                .filter(s -> !bookingRequest.getBookingSlots().contains(s))
                .collect(Collectors.toList()));
        BookingSlots updatedSlots = mongoTemplate.save(bookingSlot);
        log.info("Updated booking slots: {} for: {}", updatedSlots, bookingRequest.getBookingDate());

        BookingEntity booking = new BookingEntity();
        booking.setCustomerId(customer.getId());
        booking.setStaffId(staff.getId());
        booking.setServiceId(bookingRequest.getServiceId());
        booking.setBookingDate(bookingRequest.getBookingDate());
        booking.setBookingSlots(bookingRequest.getBookingSlots());
        booking.setNotifyCustomer(bookingRequest.isNotifyCustomer());

        BookingEntity persisted = bookingRepository.save(booking);

        // needed for AOP after return statement to queue message
        @SuppressWarnings("UnnecessaryLocalVariable")
        BookingResponse bookingResponse = BookingResponse.builder()
                .message("Successfully created booking")
                .booking(BookingDTO.builder()
                        .bookingId(persisted.getId())
                        .customerEmail(customer.getEmail())
                        .staffEmail(staff.getEmail())
                        .date(LocalDate.now().toString())
                        .timeslots(booking.getBookingSlots())
                        .build())
                .build();
        return bookingResponse;
    }

    /**
     * Check the selected timeslots are still available
     * in timeslots collection for the specific seller
     * @param bookingSlots list of selected timeslots
     * @return true or false
     */
    private boolean isTimeslotValid(List<String> bookingSlots, BookingSlots bookingSlot) {
        List<String> requestSlotInExistingSlots = bookingSlots
                .stream().filter(s -> bookingSlot.getTimeSlots().contains(s))
                .collect(Collectors.toList());
        log.debug("requestSlotInExistingSlots: {}", requestSlotInExistingSlots);

        return !requestSlotInExistingSlots.isEmpty();
    }
}
