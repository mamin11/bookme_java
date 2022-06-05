package theoneamin.bookings.backend.api.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import theoneamin.bookings.backend.api.config.UserEndpoints;
import theoneamin.bookings.backend.api.entity.user.UserEntity;
import theoneamin.bookings.backend.api.service.UserService;

import java.util.List;

@RestController
@RequestMapping()
@CrossOrigin(origins = "*")
@Slf4j
@Validated
public class UserController {
    @Autowired UserService userService;

    @GetMapping(UserEndpoints.USERS_ALL)
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        log.info("{} request", UserEndpoints.USERS_ALL);
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
    }

    @GetMapping(UserEndpoints.USERS_STAFF)
    public ResponseEntity<List<UserEntity>> getAllStaff() {
        log.info("{} request", UserEndpoints.USERS_STAFF);
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllStaff());
    }

    @GetMapping(UserEndpoints.USERS_CUSTOMER)
    public ResponseEntity<List<UserEntity>> getAllCustomers() {
        log.info("{} request", UserEndpoints.USERS_CUSTOMER);
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllCustomers());
    }
}
