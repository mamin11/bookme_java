package theoneamin.bookings.backend.api.controllers;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import theoneamin.bookings.backend.api.config.CustomerEndpoints;
import theoneamin.bookings.backend.api.entity.user.request.CreateUserRequest;
import theoneamin.bookings.backend.api.entity.user.CustomerEntity;
import theoneamin.bookings.backend.api.entity.user.response.UserResponse;
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
    public ResponseEntity<List<CustomerEntity>> getAllCustomers() {
        log.info("{} request", CustomerEndpoints.USERS_CUSTOMER);
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getAllCustomers());
    }

    @PostMapping(CustomerEndpoints.CUSTOMER_ADD)
    public ResponseEntity<UserResponse> addCustomer(@Valid @RequestBody CreateUserRequest createUserRequest) {
        log.info("{} request", CustomerEndpoints.CUSTOMER_ADD);
        return customerService.addCustomer(createUserRequest);
    }

    @GetMapping(CustomerEndpoints.CUSTOMER_GET)
    public ResponseEntity<UserResponse> getCustomer(@NonNull @RequestBody String email) {
        log.info("{} request", CustomerEndpoints.CUSTOMER_GET);
        return customerService.getCustomerByEmail(email);
    }

    @PutMapping(CustomerEndpoints.CUSTOMER_EDIT)
    public ResponseEntity<UserResponse> editCustomer(@Valid @RequestBody CreateUserRequest createUserRequest) {
        log.info("{} request", CustomerEndpoints.CUSTOMER_EDIT);
        return customerService.editCustomer(createUserRequest);
    }

    @DeleteMapping(CustomerEndpoints.CUSTOMER_DELETE)
    public ResponseEntity<String> deleteCustomer(@RequestBody String email) {
        log.info("{} request", CustomerEndpoints.CUSTOMER_DELETE);
        return customerService.deleteCustomer(email);
    }
}
