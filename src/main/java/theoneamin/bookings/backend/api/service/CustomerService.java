package theoneamin.bookings.backend.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import theoneamin.bookings.backend.api.entity.user.response.UserDTO;
import theoneamin.bookings.backend.api.entity.user.request.CreateUserRequest;
import theoneamin.bookings.backend.api.entity.user.CustomerEntity;
import theoneamin.bookings.backend.api.entity.user.response.UserResponse;
import theoneamin.bookings.backend.api.enums.UserType;
import theoneamin.bookings.backend.api.exception.ApiException;
import theoneamin.bookings.backend.api.repository.CustomerRepository;

import java.util.List;

@Service
@Slf4j
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;


    /**
     * Get all customers
     * @return list of customers
     */
    public List<CustomerEntity> getAllCustomers() {
        return customerRepository.findAllByUserType(UserType.CUSTOMER);
    }

    /**
     * Add customer to the database
     * @param createUserRequest payload
     * @return added user and message
     */
    public ResponseEntity<UserResponse> addCustomer(CreateUserRequest createUserRequest) {
        //validate email not taken
        customerRepository.findByEmail(createUserRequest.getEmail()).ifPresent(userEntity -> {throw new ApiException("Email already taken");});

        CustomerEntity user = new CustomerEntity();
        user.setFirstname(createUserRequest.getFirstname());
        user.setLastname(createUserRequest.getLastname());
        user.setEmail(createUserRequest.getEmail());
        user.setUserType(UserType.getUserType(createUserRequest.getUserType()));
        user.setPassword(createUserRequest.getPassword());

        CustomerEntity savedUser = customerRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK)
                .body(UserResponse.builder()
                                .message("successfully added user")
                                .user(UserDTO.builder()
                                        .id(savedUser.getUserId())
                                        .firstname(savedUser.getFirstname())
                                        .lastname(savedUser.getLastname())
                                        .email(savedUser.getEmail())
                                        .phone(savedUser.getPhone())
                                        .image(savedUser.getImage())
                                        .userType(savedUser.getUserType())
                                        .allTimeBookings(0)
                                        .build())
                                .build());
    }

    /**
     * Get customer by email
     * @param email email address
     * @return user response
     */
    public ResponseEntity<UserResponse> getCustomerByEmail(String email) {
        CustomerEntity user = customerRepository.findByEmail(email).orElseThrow(() -> new ApiException("User does not exist"));
        return ResponseEntity.status(HttpStatus.OK)
                .body(UserResponse.builder()
                        .message("success")
                        .user(UserDTO.builder()
                                .id(user.getUserId())
                                .firstname(user.getFirstname())
                                .lastname(user.getLastname())
                                .email(user.getEmail())
                                .phone(user.getPhone())
                                .image(user.getImage())
                                .userType(user.getUserType())
                                .allTimeBookings(0)
                                .build())
                        .build());
    }

    /**
     * Edit customer details
     * @param createUserRequest payload
     * @return user response
     */
    public ResponseEntity<UserResponse> editCustomer(CreateUserRequest createUserRequest) {
        // validate customer email
        CustomerEntity user = customerRepository.findByEmail(createUserRequest.getEmail()).orElseThrow(() -> new ApiException("User does not exist"));
        BeanUtils.copyProperties(createUserRequest, user);
        customerRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK)
                .body(UserResponse.builder().message("User updated")
                        .user(UserDTO.builder()
                                .id(user.getUserId())
                                .firstname(user.getFirstname())
                                .lastname(user.getLastname())
                                .email(user.getEmail())
                                .phone(user.getPhone())
                                .image(user.getImage())
                                .userType(user.getUserType())
                                .allTimeBookings(0)
                                .build())
                        .build());
    }

    public ResponseEntity<String> deleteCustomer(String email) {
        CustomerEntity user = customerRepository.findByEmail(email).orElseThrow(() -> new ApiException("User does not exist"));
        customerRepository.delete(user);

        return ResponseEntity.status(HttpStatus.OK).body("User deleted");
    }
}
