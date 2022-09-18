package theoneamin.bookings.backend.api.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import theoneamin.bookings.backend.api.config.StaffEndpoints;
import theoneamin.bookings.backend.api.entity.user.request.EditUserRequest;
import theoneamin.bookings.backend.api.entity.user.response.UserDTO;
import theoneamin.bookings.backend.api.entity.user.request.CreateUserRequest;
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
    public ResponseEntity<List<UserDTO>> getAllStaff() {
        log.info("{} request", StaffEndpoints.USERS_STAFF);
        return ResponseEntity.status(HttpStatus.OK).body(staffService.getAllStaff());
    }

    @PostMapping(StaffEndpoints.STAFF_ADD)
    public ResponseEntity<UserResponse> addStaff(@Valid @RequestBody CreateUserRequest createUserRequest) {
//        @RequestParam("image") MultipartFile file
        log.info("{} request", StaffEndpoints.STAFF_ADD);
        return staffService.addStaff(createUserRequest);
    }

    @PutMapping(StaffEndpoints.STAFF_EDIT)
    public ResponseEntity<UserResponse> editStaff(@PathVariable Integer id, @Valid @RequestBody EditUserRequest editUserRequest) {
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