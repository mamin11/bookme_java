package theoneamin.bookings.backend.api.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import theoneamin.bookings.backend.api.config.StaffEndpoints;
import theoneamin.bookings.backend.api.entity.user.request.CreateStaffRequest;
import theoneamin.bookings.backend.api.entity.user.request.EditStaffRequest;
import theoneamin.bookings.backend.api.entity.user.response.StaffDTO;
import theoneamin.bookings.backend.api.entity.user.response.UserResponse;
import theoneamin.bookings.backend.api.service.StaffService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping()
@CrossOrigin(origins = "*")
@Slf4j
@Validated
public class StaffController {
    @Autowired StaffService staffService;

    @GetMapping(StaffEndpoints.USERS_STAFF)
    public ResponseEntity<List<StaffDTO>> getPageStaff(@PathVariable Integer pageNumber) {
        log.info("{} request", StaffEndpoints.USERS_STAFF);
        return ResponseEntity.status(HttpStatus.OK).body(staffService.getPageStaff(pageNumber));
    }

    @GetMapping(StaffEndpoints.USERS_STAFF_ALL)
    public ResponseEntity<List<StaffDTO>> getAllStaff() {
        log.info("{} request", StaffEndpoints.USERS_STAFF);
        return ResponseEntity.status(HttpStatus.OK).body(staffService.getAllStaff());
    }

    @GetMapping(StaffEndpoints.STAFF_SEARCH)
    public ResponseEntity<List<StaffDTO>> searchStaff(@PathVariable String name) {
        log.info("{} request", StaffEndpoints.STAFF_SEARCH);
        return ResponseEntity.status(HttpStatus.OK).body(staffService.searchStaff(name));
    }

    @GetMapping(StaffEndpoints.PAGE_SIZE)
    public ResponseEntity<Integer> pageSize() {
        log.info("{} request", StaffEndpoints.PAGE_SIZE);
        return ResponseEntity.status(HttpStatus.OK).body(staffService.getMaxPageSize());
    }

    @PostMapping(path = StaffEndpoints.STAFF_ADD, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<UserResponse> addStaff(@Valid @ModelAttribute CreateStaffRequest createStaffRequest) {
        log.info("{} request", StaffEndpoints.STAFF_ADD);
        return staffService.addStaff(createStaffRequest);
    }

    @PutMapping(path = StaffEndpoints.STAFF_EDIT, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<UserResponse> editStaff(@PathVariable Integer id, @Valid @ModelAttribute EditStaffRequest editUserRequest) {
        log.info("{} request", StaffEndpoints.STAFF_EDIT);
        return staffService.editStaff(id, editUserRequest);
    }

    @DeleteMapping(StaffEndpoints.STAFF_DELETE)
    public ResponseEntity<String> deleteStaff(@PathVariable Integer id) {
        log.info("{} request: {}", StaffEndpoints.STAFF_DELETE, id);
        staffService.deleteStaff(id);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted user");
    }
}
