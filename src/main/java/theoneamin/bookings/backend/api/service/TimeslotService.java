package theoneamin.bookings.backend.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import theoneamin.bookings.backend.api.entity.timeslot.StaffTimeslot;
import theoneamin.bookings.backend.api.entity.timeslot.request.TimeslotCreateRequest;
import theoneamin.bookings.backend.api.entity.timeslot.request.TimeslotRequest;
import theoneamin.bookings.backend.api.entity.timeslot.response.TimeSlotResponse;
import theoneamin.bookings.backend.api.entity.user.StaffEntity;
import theoneamin.bookings.backend.api.exception.ApiException;
import theoneamin.bookings.backend.api.repository.StaffRepository;
import theoneamin.bookings.backend.api.repository.StaffTimeslotRepository;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TimeslotService {
    @Autowired StaffTimeslotRepository staffTimeslotRepository;
    @Autowired StaffRepository staffRepository;

    /**
     * Get all timeslots
     * @return list of timeslots
     * @param timeslotRequest timeslot request
     */
    public List<StaffTimeslot> getAllTimeslots(@Valid TimeslotRequest timeslotRequest) {
        StaffEntity staffEntity = staffRepository.findByUserId(timeslotRequest.getStaffId())
                .orElseThrow(() -> new ApiException("Staff with id: "+timeslotRequest.getStaffId()+" does not exist"));
        return staffTimeslotRepository.findByStaffAndBookingDate(staffEntity, LocalDate.parse(timeslotRequest.getBookingDate()));
    }

    /**
     * Add timeslot entity
     * @param timeslotRequest timeslot request
     * @return timeslot response
     */
    @Transactional
    public TimeSlotResponse addTimeslot(TimeslotCreateRequest timeslotRequest) {
        List<LocalTime> mappedTimeList = timeslotRequest.getTime()
                .stream()
                .map(LocalTime::parse)
                .sorted(LocalTime::compareTo)
                .collect(Collectors.toList());

        LocalDate bookingDate = LocalDate.parse(timeslotRequest.getBookingDate());

        // validate
        StaffEntity staff = staffRepository.findByUserId(timeslotRequest.getStaffId())
                .orElseThrow(() -> new ApiException("Staff with id: "+timeslotRequest.getStaffId()+" does not exist"));

//        List<StaffTimeslot> staffTimeslots = staffTimeslotRepository
//                .findByStaffAndBookingDateAndBookingTimeIn(staff, bookingDate, mappedTimeList);

        List<LocalTime> staffSavedTimeSlots = staff.getTimeslots().stream().map(StaffTimeslot::getBookingTime).collect(Collectors.toList());

        // get timeslots in request not in db - create them
        List<LocalTime> timeslotsToCreate = mappedTimeList
                .stream()
                .filter(time -> !staffSavedTimeSlots.contains(time))
                .collect(Collectors.toList());
        log.debug("Timeslots to create for staff: {} :{}", staff.getUserId(), timeslotsToCreate);

        // get timeslots in db not in request - delete them
        List<LocalTime> timeslotsToDelete = staffSavedTimeSlots
                .stream()
                .filter(time -> !mappedTimeList.contains(time))
                .collect(Collectors.toList());
        log.debug("Timeslots to delete for staff: {} :{}", staff.getUserId(), timeslotsToDelete);

//        if (!staffTimeslots.isEmpty()) {
//            throw new ApiException("One or more time slots already exist for staff: "+staff.getUserId());
//        }

        // create
        timeslotsToCreate.forEach(time -> {
            StaffTimeslot timeslot = new StaffTimeslot();
            timeslot.setStaff(staff);
            timeslot.setBookingDate(bookingDate);
            timeslot.setBookingTime(time);

            staffTimeslotRepository.save(timeslot);
        });
        log.debug("Saved: {} timeslots for staff: {}", timeslotsToCreate.size(), staff.getUserId());

        timeslotsToDelete.forEach(time -> {
            staffTimeslotRepository.deleteByStaffAndBookingDateAndBookingTimeIn(staff, bookingDate, timeslotsToDelete);
        });
        log.debug("Deleted: {} timeslots for staff: {}", timeslotsToDelete.size(), staff.getUserId());

        // response
        return TimeSlotResponse
                .builder()
                .message("Timeslots added")
                .timeslot(staffTimeslotRepository.findByStaffAndBookingDate(staff, bookingDate))
                .build();
    }

    /**
     * Edit service entity
     * @param id id of the timeslot to edit
     * @param staffTimeslot timeslot entity
     * @return timeslot response
     */
    public TimeSlotResponse editTimeslot(Integer id, StaffTimeslot staffTimeslot) {


        // return
        return TimeSlotResponse
                .builder()
                .message("Service updated")
                .timeslot(null)
                .build();
    }

    /**
     * Delete timeslot entity
     * @param id id of the timeslot to delete
     */
    public void deleteTimeslot(Integer id) {
        StaffTimeslot staffTimeslot = staffTimeslotRepository.findById(id).orElseThrow(() -> new ApiException("Timeslot not found by id"));
        staffTimeslotRepository.delete(staffTimeslot);
        log.info("Timeslot deleted");
    }
}
