package theoneamin.bookings.backend.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import theoneamin.bookings.backend.api.entity.user.UserRequest;
import theoneamin.bookings.backend.api.entity.user.UserEntity;
import theoneamin.bookings.backend.api.entity.user.UserResponse;
import theoneamin.bookings.backend.api.enums.UserType;
import theoneamin.bookings.backend.api.exception.ApiException;
import theoneamin.bookings.backend.api.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
public class CustomerService {

    @Autowired
    UserRepository userRepository;


    /**
     * Get all customers
     * @return list of customers
     */
    public List<UserEntity> getAllCustomers() {
        return userRepository.findAllByUserType(UserType.CUSTOMER);
    }

    /**
     * Add customer to the database
     * @param userRequest payload
     * @return added user and message
     */
    public ResponseEntity<UserResponse> addCustomer(UserRequest userRequest) {
        //validate email not taken
        userRepository.findByEmail(userRequest.getEmail()).ifPresent(userEntity -> {throw new ApiException("Email already taken");});

        UserEntity user = new UserEntity();
        user.setFirstname(userRequest.getFirstname());
        user.setLastname(userRequest.getLastname());
        user.setEmail(userRequest.getEmail());
        user.setUserType(userRequest.getUserType());
        user.setPassword(userRequest.getPassword());

        return ResponseEntity.status(HttpStatus.OK)
                .body(UserResponse.builder()
                                .message("successfully added user")
                                .user(userRepository.save(user))
                                .build());
    }

    /**
     * Get customer by email
     * @param email email address
     * @return user response
     */
    public ResponseEntity<UserResponse> getCustomerByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException("User does not exist"));
        return ResponseEntity.status(HttpStatus.OK)
                .body(UserResponse.builder()
                        .message("success")
                        .user(user)
                        .build());
    }

    /**
     * Edit customer details
     * @param userRequest payload
     * @return user response
     */
    public ResponseEntity<UserResponse> editCustomer(UserRequest userRequest) {
        // validate customer email
        UserEntity user = userRepository.findByEmail(userRequest.getEmail()).orElseThrow(() -> new ApiException("User does not exist"));
        BeanUtils.copyProperties(userRequest, user);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK)
                .body(UserResponse.builder().message("User updated")
                        .user(user)
                        .build());
    }

    public ResponseEntity<String> deleteCustomer(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException("User does not exist"));
        userRepository.delete(user);

        return ResponseEntity.status(HttpStatus.OK).body("User deleted");
    }
}
