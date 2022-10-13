package theoneamin.bookings.backend.api.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import theoneamin.bookings.backend.api.config.TimeslotEndpoints;
import theoneamin.bookings.backend.api.entity.timeslot.StaffTimeslot;
import theoneamin.bookings.backend.api.entity.timeslot.request.TimeslotCreateRequest;
import theoneamin.bookings.backend.api.entity.timeslot.request.TimeslotRequest;
import theoneamin.bookings.backend.api.entity.timeslot.response.TimeSlotResponse;
import theoneamin.bookings.backend.api.service.TimeslotService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping()
@CrossOrigin(origins = "*")
@Slf4j
@Validated
public class TimeslotController {
    @Autowired
    TimeslotService timeslotService;

    @PostMapping(TimeslotEndpoints.STAFF_TIMESLOT_ALL)
    public ResponseEntity<List<StaffTimeslot>> getAllStaffTimeslots(@Valid @RequestBody TimeslotRequest timeslotRequest) {
        log.info("{} request", TimeslotEndpoints.STAFF_TIMESLOT_ALL);
        return ResponseEntity.status(HttpStatus.OK).body(timeslotService.getAllTimeslots(timeslotRequest));
    }

    @PostMapping(TimeslotEndpoints.TIMESLOT_ADD)
    public ResponseEntity<TimeSlotResponse> addTimeslot(@Valid @RequestBody TimeslotCreateRequest timeslotRequest) {
        log.info("{} request: {}", TimeslotEndpoints.TIMESLOT_ADD, timeslotRequest);
        TimeSlotResponse response = timeslotService.addTimeslot(timeslotRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(TimeslotEndpoints.TIMESLOT_EDIT)
    public ResponseEntity<TimeSlotResponse> editTimeslot(@PathVariable Integer id, @RequestBody StaffTimeslot staffTimeslot) {
        log.info("{} request: {}", TimeslotEndpoints.TIMESLOT_EDIT, id);
        return ResponseEntity.status(HttpStatus.OK).body(timeslotService.editTimeslot(id, staffTimeslot));
    }

    @DeleteMapping(TimeslotEndpoints.TIMESLOT_DELETE)
    public ResponseEntity<String> deleteTimeslot(@PathVariable Integer id) {
        log.info("{} request: {}", TimeslotEndpoints.TIMESLOT_DELETE, id);
        timeslotService.deleteTimeslot(id);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted timeslot");
    }
}
