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
        UserEntity customer = validateUser(bookingRequest.getCustomerEmail(), UserType.CUSTOMER, "Customer does not exist");

        // validate staff
        UserEntity staff = validateUser(bookingRequest.getStaffEmail(), UserType.STAFF, "Staff does not exist");

        // get available slots
        BookingSlots bookingSlot = getAvailableBookingSlots(bookingRequest.getBookingDate(), staff.getId());

//        // validate date
//        if (bookingSlot == null) {
//            log.error("No booking slots available for date: {} for staff: {}", bookingRequest.getBookingDate(), bookingRequest.getStaffEmail());
//            throw new ApiException("No booking slots available for date: "+bookingRequest.getBookingDate()+" for staff: "+bookingRequest.getStaffEmail());
//        }
//
//        // validate booking time slot
//        boolean isTimeslotValid = isTimeslotValid(bookingRequest.getBookingSlots(), bookingSlot);
//        log.info("isTimeSlotValid: {}", isTimeslotValid);
//        if (!isTimeslotValid) {
//            log.error("Timeslot validation failed: {}, {}", bookingRequest.getBookingSlots(), bookingRequest.getBookingDate());
//            throw new ApiException("One or more of selected timeslots is already taken: "+bookingRequest.getBookingSlots());
//        }

        // remove selected bookings from timeslots for the day
//        bookingSlot.setTimeSlots(bookingSlot
//                .getTimeSlots()
//                .stream()
//                .filter(s -> !bookingRequest.getBookingSlots().contains(s))
//                .collect(Collectors.toList()));
//        BookingSlots updatedSlots = mongoTemplate.save(bookingSlot);
//        log.info("Updated booking slots: {} for: {}", updatedSlots, bookingRequest.getBookingDate());

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
                        .customerFirstName(customer.getFirstname())
                        .customerEmail(customer.getEmail())
                        .staffEmail(staff.getEmail())
                        .date(persisted.getBookingDate())
                        .notifyCustomer(persisted.isNotifyCustomer())
                        .timeslots(booking.getBookingSlots())
                        .build())
                .build();
        return bookingResponse;
    }

    private BookingSlots getAvailableBookingSlots(String bookingDate, Integer id) {
        Criteria criteria = Criteria.where("booking_date").is(bookingDate)
                .andOperator(Criteria.where("seller_id").is(id));
        BookingSlots bookingSlot = mongoTemplate.findOne(Query.query(criteria), BookingSlots.class);
        log.debug("Found booking slots: {} for date: {}", bookingSlot, bookingDate);
        return bookingSlot;
    }

    private UserEntity validateUser(String email, UserType userType, String errorMessage) {
        return userRepository
                .findByEmailAndUserType(email, userType)
                .orElseThrow(() -> new ApiException(errorMessage));
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

    /**
     * Get booking by id
     * @param id booking id
     * @return booking
     */
    public BookingEntity getBookingById(String id) {
        return bookingRepository.findById(id).orElseThrow(() -> new ApiException("Booking not found by id"));
    }

    /**
     * Get all bookings
     * @return list of bookings
     */
    public List<BookingEntity> getAllBookings() {
        return bookingRepository.findAll();
    }

    /**
     * Get bookings by staff id
     * @param id staff id
     * @return list of bookings
     */
    public List<BookingEntity> getByStaffId(Integer id) {
        return mongoTemplate.find(Query.query(Criteria.where("staffId").is(id)), BookingEntity.class);
    }

    /**
     * Get bookings by customer id
     * @param id customer id
     * @return list of bookings
     */
    public List<BookingEntity> getByCustomerId(Integer id) {
        return mongoTemplate.find(Query.query(Criteria.where("customerId").is(id)), BookingEntity.class);
    }

    /**
     * Get bookings by service id
     * @param id service id
     * @return list of bookings
     */
    public List<BookingEntity> getByServiceId(Integer id) {
        return mongoTemplate.find(Query.query(Criteria.where("serviceId").is(id)), BookingEntity.class);
    }

    /**
     * Get bookings by date
     * @param date date
     * @return list of bookings
     */
    public List<BookingEntity> getByDate(String date) {
        return mongoTemplate.find(Query.query(Criteria.where("bookingDate").is(date)), BookingEntity.class);
    }

    /**
     * Edit booking
     * @param id booking id
     * @param booking booking
     * @return updated booking
     */
    //TODO: optimize method. (speed, database interactions)
    public BookingEntity editBooking(String id, BookingEntity booking) {
        BookingEntity bookingEntity = bookingRepository.findById(id).orElseThrow(() -> new ApiException("Booking not found by id"));
        log.info("Booking validated: {}", bookingEntity.getId());

        // validate customer
        UserEntity customer = userRepository.findById(booking.getCustomerId()).orElseThrow(() -> new ApiException("Customer not found by id"+booking.getCustomerId()));
        log.info("Customer validated: {}", customer.getId());

        // validate staff
        UserEntity staff = userRepository.findById(booking.getStaffId()).orElseThrow(() -> new ApiException("Staff not found by id"+booking.getStaffId()));
        log.info("Staff validated: {}", staff.getId());

        // get available slots
        BookingSlots bookingSlot = getAvailableBookingSlots(booking.getBookingDate(), staff.getId());

        // validate date
        if (bookingSlot == null) {
            log.error("No booking slots available for date: {} for staff: {}", booking.getBookingDate(), booking.getStaffId());
            throw new ApiException("No booking slots available for date: "+booking.getBookingDate()+" for staff: "+booking.getStaffId());
        }

        if ( booking.getBookingSlots().containsAll(bookingSlot.getTimeSlots()) && bookingSlot.getTimeSlots().containsAll(booking.getBookingSlots())) {
            log.info("Booking timeslots different. Validating timeslots");
            // validate booking time slot
            boolean isTimeslotValid = isTimeslotValid(booking.getBookingSlots(), bookingSlot);
            log.info("isTimeSlotValid: {}", isTimeslotValid);
            if (!isTimeslotValid) {
                log.error("Timeslot validation failed: {}, {}", booking.getBookingSlots(), booking.getBookingDate());
                throw new ApiException("One or more of selected timeslots is already taken: " + booking.getBookingSlots());
            }
        }

        // if date changed, put booking time slots back in previous date
        if (!booking.getBookingDate().equals(bookingEntity.getBookingDate())) {
            log.info("Booking date is different. Updating previous date timeslots");
            BookingSlots prevBookingSlot = getAvailableBookingSlots(bookingEntity.getBookingDate(), bookingEntity.getStaffId());

            if (prevBookingSlot == null) {
                log.error("Booking slots not found for date: {} seller_id: {}",bookingEntity.getBookingDate(),bookingEntity.getStaffId());
                throw new ApiException("Booking slots not found for date: "+bookingEntity.getBookingDate()+" seller_id: "+bookingEntity.getStaffId());
            }

            // add if not already exist
            prevBookingSlot.getTimeSlots().addAll(booking.getBookingSlots());
            prevBookingSlot.setTimeSlots(
                    prevBookingSlot.getTimeSlots()
                            .stream()
                            .distinct()
                            .collect(Collectors.toList()));

            // remove selected bookings from timeslots for the day
            bookingSlot.setTimeSlots(bookingSlot
                    .getTimeSlots()
                    .stream()
                    .filter(s -> !booking.getBookingSlots().contains(s))
                    .collect(Collectors.toList()));
            BookingSlots updatedSlots = mongoTemplate.save(bookingSlot);
            log.info("Updated booking slots: {} for: {}", updatedSlots, booking.getBookingDate());

            mongoTemplate.save(prevBookingSlot);

        }

        bookingEntity.setBookingDate(booking.getBookingDate());
        bookingEntity.setBookingSlots(booking.getBookingSlots());
        bookingEntity.setServiceId(booking.getServiceId());
        bookingEntity.setStaffId(booking.getStaffId());
        bookingEntity.setCustomerId(booking.getCustomerId());
        bookingEntity.setNotifyCustomer(booking.isNotifyCustomer());

        BookingEntity updatedBooking = mongoTemplate.save(bookingEntity);
        log.info("Updated booking: {}", updatedBooking);

        return updatedBooking;
    }

    /**
     * Delete booking by id
     * @param id booking id
     */
    public void deleteBooking(String id) {
        BookingEntity bookingEntity = bookingRepository.findById(id).orElseThrow(() -> new ApiException("Booking not found by id"));
        log.info("Booking validated: {}", bookingEntity.getId());
        mongoTemplate.remove(bookingEntity);
        log.info("Booking deleted");
    }
}
