package theoneamin.bookings.backend.api.controllers;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import theoneamin.bookings.backend.api.config.CustomerEndpoints;
import theoneamin.bookings.backend.api.entity.user.request.CreateUserRequest;
import theoneamin.bookings.backend.api.entity.user.CustomerEntity;
import theoneamin.bookings.backend.api.entity.user.request.EditUserRequest;
import theoneamin.bookings.backend.api.entity.user.response.StaffDTO;
import theoneamin.bookings.backend.api.entity.user.response.UserDTO;
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
    public ResponseEntity<List<UserDTO>> getAllCustomers(@PathVariable Integer pageNumber) {
        log.info("{} request", CustomerEndpoints.USERS_CUSTOMER);
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getAllCustomers(pageNumber));
    }

    @GetMapping(CustomerEndpoints.CUSTOMER_SEARCH)
    public ResponseEntity<List<UserDTO>> searchCustomer(@PathVariable String name) {
        log.info("{} request", CustomerEndpoints.CUSTOMER_SEARCH);
        return ResponseEntity.status(HttpStatus.OK).body(customerService.searchCustomer(name));
    }

    @GetMapping(CustomerEndpoints.PAGE_SIZE)
    public ResponseEntity<Integer> pageSize() {
        log.info("{} request", CustomerEndpoints.PAGE_SIZE);
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getMaxPageSize());
    }

    @PostMapping(path = CustomerEndpoints.CUSTOMER_ADD, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<UserResponse> addCustomer(@Valid @ModelAttribute CreateUserRequest createUserRequest) {
        log.info("{} request", CustomerEndpoints.CUSTOMER_ADD);
        return customerService.addCustomer(createUserRequest);
    }

    @GetMapping(CustomerEndpoints.CUSTOMER_GET)
    public ResponseEntity<UserResponse> getCustomer(@NonNull @RequestBody String email) {
        log.info("{} request", CustomerEndpoints.CUSTOMER_GET);
        return customerService.getCustomerByEmail(email);
    }

    @PutMapping(path = CustomerEndpoints.CUSTOMER_EDIT, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<UserResponse> editCustomer(@PathVariable Integer id, @Valid @ModelAttribute EditUserRequest createUserRequest) {
        log.info("{} request", CustomerEndpoints.CUSTOMER_EDIT);
        return customerService.editCustomer(id, createUserRequest);
    }

    @DeleteMapping(CustomerEndpoints.CUSTOMER_DELETE)
    public ResponseEntity<String> deleteCustomer(@PathVariable Integer id) {
        log.info("{} request", CustomerEndpoints.CUSTOMER_DELETE);
        return customerService.deleteCustomer(id);
    }
}
