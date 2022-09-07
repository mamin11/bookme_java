package theoneamin.bookings.backend.api.controllers;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import theoneamin.bookings.backend.api.config.CustomerEndpoints;
import theoneamin.bookings.backend.api.entity.user.UserRequest;
import theoneamin.bookings.backend.api.entity.user.UserEntity;
import theoneamin.bookings.backend.api.entity.user.UserResponse;
import theoneamin.bookings.backend.api.service.CustomerService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping()
@CrossOrigin(origins = "*")
@Slf4j
@Validated
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @GetMapping(CustomerEndpoints.USERS_CUSTOMER)
    public ResponseEntity<List<UserEntity>> getAllCustomers() {
        log.info("{} request", CustomerEndpoints.USERS_CUSTOMER);
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getAllCustomers());
    }

    @PostMapping(CustomerEndpoints.CUSTOMER_ADD)
    public ResponseEntity<UserResponse> addCustomer(@Valid @RequestBody UserRequest userRequest) {
        log.info("{} request", CustomerEndpoints.CUSTOMER_ADD);
        return customerService.addCustomer(userRequest);
    }

    @GetMapping(CustomerEndpoints.CUSTOMER_GET)
    public ResponseEntity<UserResponse> getCustomer(@NonNull @RequestBody String email) {
        log.info("{} request", CustomerEndpoints.CUSTOMER_GET);
        return customerService.getCustomerByEmail(email);
    }

    @PutMapping(CustomerEndpoints.CUSTOMER_EDIT)
    public ResponseEntity<UserResponse> editCustomer(@Valid @RequestBody UserRequest userRequest) {
        log.info("{} request", CustomerEndpoints.CUSTOMER_EDIT);
        return customerService.editCustomer(userRequest);
    }

    @DeleteMapping(CustomerEndpoints.CUSTOMER_DELETE)
    public ResponseEntity<String> deleteCustomer(@RequestBody String email) {
        log.info("{} request", CustomerEndpoints.CUSTOMER_DELETE);
        return customerService.deleteCustomer(email);
    }
}
