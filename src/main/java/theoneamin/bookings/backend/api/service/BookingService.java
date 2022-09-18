package theoneamin.bookings.backend.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import theoneamin.bookings.backend.api.entity.booking.*;
import theoneamin.bookings.backend.api.entity.services.ServiceEntity;
import theoneamin.bookings.backend.api.entity.user.CustomerEntity;
import theoneamin.bookings.backend.api.entity.user.StaffEntity;
import theoneamin.bookings.backend.api.entity.user.UserEntity;
import theoneamin.bookings.backend.api.enums.UserType;
import theoneamin.bookings.backend.api.exception.ApiException;
import theoneamin.bookings.backend.api.repository.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookingService {
    @Autowired private BookingRepository bookingRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private StaffRepository staffRepository;
    @Autowired private ServiceRepository serviceRepository;
    @Autowired private MongoTemplate mongoTemplate;
    @Autowired private StaffTimeslotRepository staffTimeslotRepository;

    /**
     * Create a booking
     * @param bookingRequest booking request
     * @return booking response
     */
    @Transactional
    public BookingResponse createBooking(BookingRequest bookingRequest) {
        LocalDate bookingDate = LocalDate.parse(bookingRequest.getBookingDate());
        List<LocalTime> requestBookingSlots = bookingRequest.getBookingSlots()
                .stream()
                .map(s -> Arrays.stream(s.split(" - ")).collect(Collectors.toList()).get(0))
                .map(LocalTime::parse)
                .sorted(LocalTime::compareTo)
                .collect(Collectors.toList());

        // validate customer
        CustomerEntity customer = (CustomerEntity) validateCustomer(bookingRequest.getCustomerEmail());

        // validate staff
        StaffEntity staff = (StaffEntity) validateStaff(bookingRequest.getStaffEmail());

        // validate service
        ServiceEntity service = serviceRepository.findById(bookingRequest.getServiceId()).orElseThrow(() -> new ApiException("Service does not exist"));

        // get available slots for given staff
        List<StaffTimeslot> bookingSlots = getAvailableStaffTimeslots(bookingDate, staff);

        // validate date
        if (bookingSlots.isEmpty()) {
            log.error("No booking slots available for date: {} for staff: {}", bookingRequest.getBookingDate(), bookingRequest.getStaffEmail());
            throw new ApiException("No booking slots available for date: "+bookingRequest.getBookingDate()+" for staff: "+bookingRequest.getStaffEmail());
        }

//        // validate booking time slot
        boolean isTimeslotValid = isTimeslotValid(requestBookingSlots, bookingSlots);
        log.info("isTimeSlotValid: {}", isTimeslotValid);
        if (!isTimeslotValid) {
            log.error("Timeslot validation failed: {}, {}", requestBookingSlots, bookingRequest.getBookingDate());
            throw new ApiException("One or more of selected timeslots is already taken: "+ requestBookingSlots);
        }

        // remove selected bookings from timeslots for the day
        staffTimeslotRepository.deleteByStaffAndBookingDateAndBookingTimeIn(staff, bookingDate, requestBookingSlots);
        log.info("Removed: {} from processing date: {} timeslots for staff: {}", requestBookingSlots, bookingDate, staff.getUserId());

        BookingEntity booking = new BookingEntity();
        booking.setCustomer(customer);
        booking.setStaff(staff);
        booking.setService(service);
        booking.setBookingDate(bookingRequest.getBookingDate());
        booking.setStartTime(requestBookingSlots.get(0));
        // end time is slot start-time + 1hr, each item is slot start-time
        booking.setEndTime(requestBookingSlots.get(requestBookingSlots.size()-1).plus(1, ChronoUnit.HOURS));
        booking.setStatus(false);
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
                        .timeslots(requestBookingSlots.stream().map(LocalTime::toString).collect(Collectors.toList()))
                        .build())
                .build();
        return bookingResponse;
    }

    private List<StaffTimeslot> getAvailableStaffTimeslots(LocalDate bookingDate, StaffEntity staff) {
        List<StaffTimeslot> staffTimeslots = staffTimeslotRepository.findByStaffAndBookingDate(staff, bookingDate);
        log.debug("Found booking slots: {} for date: {}", staffTimeslots, bookingDate);
        return staffTimeslots;
    }

    private UserEntity validateCustomer(String email) {
        return customerRepository
                .findByEmailAndUserType(email, UserType.CUSTOMER)
                .orElseThrow(() -> new ApiException("Customer does not exist"));
    }

    private UserEntity validateStaff(String email) {
        return staffRepository
                .findByEmailAndUserType(email, UserType.STAFF)
                .orElseThrow(() -> new ApiException("Staff does not exist"));
    }

    /**
     * Check the selected timeslots are still available
     * in timeslots table for the specific seller
     * @param bookingSlots list of selected timeslots
     * @param bookingSlotsInDb list of timeslots in DB for the staff
     * @return true or false
     */
    private boolean isTimeslotValid(List<LocalTime> bookingSlots, List<StaffTimeslot> bookingSlotsInDb) {
        List<LocalTime> requestSlotInExistingSlots = bookingSlots
                .stream()
                .filter(s -> bookingSlotsInDb.stream()
                        .map(StaffTimeslot::getBookingTime)
                        .collect(Collectors.toList())
                        .contains(s))
                .collect(Collectors.toList());
        log.debug("requestSlotInExistingSlots: {}", requestSlotInExistingSlots);

        return !requestSlotInExistingSlots.isEmpty();
    }

    /**
     * Get booking by id
     * @param id booking id
     * @return booking
     */
    public BookingEntity getBookingById(Integer id) {
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
//    public BookingEntity editBooking(String id, BookingEntity booking) {
//        BookingEntity bookingEntity = bookingRepository.findById(id).orElseThrow(() -> new ApiException("Booking not found by id"));
//        log.info("Booking validated: {}", bookingEntity.getId());
//
//        // validate customer
//        CustomerEntity customer = customerRepository.findById(booking.getCustomerId()).orElseThrow(() -> new ApiException("Customer not found by id"+booking.getCustomerId()));
//        log.info("Customer validated: {}", customer.getUserId());
//
//        // validate staff
//        StaffEntity staff = staffRepository.findById(booking.getStaffId()).orElseThrow(() -> new ApiException("Staff not found by id"+booking.getStaffId()));
//        log.info("Staff validated: {}", staff.getUserId());
//
//        // get available slots
//        BookingSlots bookingSlot = getAvailableStaffTimeslots(booking.getBookingDate(), staff.getUserId());
//
//        // validate date
//        if (bookingSlot == null) {
//            log.error("No booking slots available for date: {} for staff: {}", booking.getBookingDate(), booking.getStaffId());
//            throw new ApiException("No booking slots available for date: "+booking.getBookingDate()+" for staff: "+booking.getStaffId());
//        }
//
////        if ( booking.getBookingSlots().containsAll(bookingSlot.getTimeSlots()) && bookingSlot.getTimeSlots().containsAll(booking.getBookingSlots())) {
////            log.info("Booking timeslots different. Validating timeslots");
////            // validate booking time slot
////            boolean isTimeslotValid = isTimeslotValid(booking.getBookingSlots(), bookingSlot);
////            log.info("isTimeSlotValid: {}", isTimeslotValid);
////            if (!isTimeslotValid) {
////                log.error("Timeslot validation failed: {}, {}", booking.getBookingSlots(), booking.getBookingDate());
////                throw new ApiException("One or more of selected timeslots is already taken: " + booking.getBookingSlots());
////            }
////        }
//
//        // if date changed, put booking time slots back in previous date
//        if (!booking.getBookingDate().equals(bookingEntity.getBookingDate())) {
//            log.info("Booking date is different. Updating previous date timeslots");
//            BookingSlots prevBookingSlot = getAvailableStaffTimeslots(bookingEntity.getBookingDate(), bookingEntity.getStaffId());
//
//            if (prevBookingSlot == null) {
//                log.error("Booking slots not found for date: {} seller_id: {}",bookingEntity.getBookingDate(),bookingEntity.getStaffId());
//                throw new ApiException("Booking slots not found for date: "+bookingEntity.getBookingDate()+" seller_id: "+bookingEntity.getStaffId());
//            }
//
//            // add if not already exist
////            prevBookingSlot.getTimeSlots().addAll(booking.getBookingSlots());
////            prevBookingSlot.setTimeSlots(
////                    prevBookingSlot.getTimeSlots()
////                            .stream()
////                            .distinct()
////                            .collect(Collectors.toList()));
//
////            // remove selected bookings from timeslots for the day
////            bookingSlot.setTimeSlots(bookingSlot
////                    .getTimeSlots()
////                    .stream()
////                    .filter(s -> !booking.getBookingSlots().contains(s))
////                    .collect(Collectors.toList()));
//            BookingSlots updatedSlots = mongoTemplate.save(bookingSlot);
//            log.info("Updated booking slots: {} for: {}", updatedSlots, booking.getBookingDate());
//
//            mongoTemplate.save(prevBookingSlot);
//
//        }
//
//        bookingEntity.setBookingDate(booking.getBookingDate());
////        bookingEntity.setBookingSlots(booking.getBookingSlots());
//        bookingEntity.setServiceId(booking.getServiceId());
//        bookingEntity.setStaffId(booking.getStaffId());
//        bookingEntity.setCustomerId(booking.getCustomerId());
//        bookingEntity.setNotifyCustomer(booking.isNotifyCustomer());
//
//        BookingEntity updatedBooking = mongoTemplate.save(bookingEntity);
//        log.info("Updated booking: {}", updatedBooking);
//
//        return updatedBooking;
//    }

    /**
     * Delete booking by id
     * @param id booking id
     */
    public void deleteBooking(Integer id) {
        BookingEntity bookingEntity = bookingRepository.findById(id).orElseThrow(() -> new ApiException("Booking not found by id"));
        log.info("Booking validated: {}", bookingEntity.getId());
        mongoTemplate.remove(bookingEntity);
        log.info("Booking deleted");
    }
}
