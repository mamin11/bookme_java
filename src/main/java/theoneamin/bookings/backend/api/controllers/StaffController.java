package theoneamin.bookings.backend.api.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import theoneamin.bookings.backend.api.config.StaffEndpoints;
import theoneamin.bookings.backend.api.entity.user.UserEntity;
import theoneamin.bookings.backend.api.entity.user.UserRequest;
import theoneamin.bookings.backend.api.entity.user.UserResponse;
import theoneamin.bookings.backend.api.service.StaffService;
import theoneamin.bookings.backend.api.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping()
@CrossOrigin(origins = "*")
@Slf4j
@Validated
public class StaffController {
    @Autowired UserService userService;
    @Autowired StaffService staffService;

    @GetMapping(StaffEndpoints.USERS_STAFF)
    public ResponseEntity<List<UserEntity>> getAllStaff() {
        log.info("{} request", StaffEndpoints.USERS_STAFF);
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllStaff());
    }

    @PostMapping(StaffEndpoints.STAFF_ADD)
    public ResponseEntity<UserResponse> addStaff(@Valid @RequestBody UserRequest userRequest) {
//        @RequestParam("image") MultipartFile file
        log.info("{} request", StaffEndpoints.STAFF_ADD);
        return staffService.addStaff(userRequest);
    }
}
