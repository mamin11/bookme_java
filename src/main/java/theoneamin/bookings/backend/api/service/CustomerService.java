package theoneamin.bookings.backend.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import theoneamin.bookings.backend.api.aws.StorageService;
import theoneamin.bookings.backend.api.entity.user.StaffEntity;
import theoneamin.bookings.backend.api.entity.user.request.EditUserRequest;
import theoneamin.bookings.backend.api.entity.user.response.StaffDTO;
import theoneamin.bookings.backend.api.entity.user.request.CreateUserRequest;
import theoneamin.bookings.backend.api.entity.user.CustomerEntity;
import theoneamin.bookings.backend.api.entity.user.response.UserDTO;
import theoneamin.bookings.backend.api.entity.user.response.UserResponse;
import theoneamin.bookings.backend.api.enums.BucketNames;
import theoneamin.bookings.backend.api.enums.FolderNames;
import theoneamin.bookings.backend.api.enums.UserType;
import theoneamin.bookings.backend.api.exception.ApiException;
import theoneamin.bookings.backend.api.repository.CustomerRepository;
import theoneamin.bookings.backend.api.utility.UtilityService;
import java.util.List;
import java.util.stream.Collectors;

import static theoneamin.bookings.backend.api.service.StaffService.PAGE_SIZE;

@Service
@Slf4j
public class CustomerService {

    @Autowired CustomerRepository customerRepository;
    @Autowired UtilityService utilityService;
    @Autowired StorageService storageService;


    /**
     * Get all customers
     * @return list of customers
     * @param pageNumber page number
     */
    public List<UserDTO> getPageCustomers(Integer pageNumber) {
        Page<CustomerEntity> userEntityList = customerRepository.findAllByUserType(UserType.CUSTOMER, PageRequest.of(pageNumber, PAGE_SIZE));
        return userEntityList.stream().map(userEntity -> UserDTO.builder()
                .id(userEntity.getUserId())
                .firstname(userEntity.getFirstname())
                .lastname(userEntity.getLastname())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .image(utilityService.getImageLink(userEntity))
                .userType(userEntity.getUserType())
                .allTimeBookings(0)
                .fullName(userEntity.getFirstname()+" "+userEntity.getLastname())
                .build()).collect(Collectors.toList());
    }

    /**
     * Get all customers
     * @return list of customers
     */
    public List<UserDTO> getAllCustomers() {
        List<CustomerEntity> userEntityList = customerRepository.findAllByUserType(UserType.CUSTOMER);
        return userEntityList.stream().map(userEntity -> UserDTO.builder()
                .id(userEntity.getUserId())
                .firstname(userEntity.getFirstname())
                .lastname(userEntity.getLastname())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .image(utilityService.getImageLink(userEntity))
                .userType(userEntity.getUserType())
                .allTimeBookings(0)
                .fullName(userEntity.getFirstname()+" "+userEntity.getLastname())
                .build()).collect(Collectors.toList());
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
        user.setPhone(createUserRequest.getPhone());

        CustomerEntity savedUser = customerRepository.save(user);
        log.info("Saved customer: {}", savedUser);

        if (createUserRequest.getImage() != null) {
            // upload image and get link
            String filename = utilityService.handleImageUpload(savedUser, createUserRequest.getImage());

            // update user image
            savedUser.setImage(filename);
            customerRepository.saveAndFlush(savedUser);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(UserResponse.builder()
                                .message("successfully added user")
                                .user(StaffDTO.builder()
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
                        .user(StaffDTO.builder()
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
     * Get all customers
     * @return list of staff
     * @param name staff name
     */
    public List<UserDTO> searchCustomer(String name) {
        List<CustomerEntity> userEntityList = customerRepository.findByUserTypeAndFirstnameContainsOrUserTypeAndLastnameContains(UserType.CUSTOMER, name, UserType.CUSTOMER, name);
        return userEntityList.stream().map(userEntity -> UserDTO.builder()
                .id(userEntity.getUserId())
                .firstname(userEntity.getFirstname())
                .lastname(userEntity.getLastname())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .image(utilityService.getImageLink(userEntity))
                .userType(userEntity.getUserType())
                .allTimeBookings(0)
                .fullName(userEntity.getFirstname()+" "+userEntity.getLastname())
                .build()).collect(Collectors.toList());
    }

    /**
     * Get max page size for customers from the database
     * @return max page size
     */
    public Integer getMaxPageSize(){
        return (int) Math.ceil((double) customerRepository.countByUserType(UserType.CUSTOMER)/PAGE_SIZE);
    }

    /**
     * Edit customer details
     * @param id customer id
     * @param createUserRequest payload
     * @return user response
     */
    public ResponseEntity<UserResponse> editCustomer(Integer id, EditUserRequest createUserRequest) {
        // validate customer email
        CustomerEntity user = customerRepository.findById(id).orElseThrow(() -> new ApiException("User does not exist"));
        BeanUtils.copyProperties(createUserRequest, user);

        if (createUserRequest.getImage() != null) {
            // upload image and get link
            String filename = utilityService.handleImageUpload(user, createUserRequest.getImage());

            // if there is previous image, delete it from store
            if (user.getImage() != null) {
                storageService.delete(BucketNames.BOOKING_APP_STORE.getStringValue(),
                        String.format("%s/%s", String.format("%s/%s", FolderNames.PROFILE_PICTURES, user.getUserId()), user.getImage()));
            }

            // update user image
            user.setImage(filename);

        } else {
            if (user.getImage() != null) {
                storageService.delete(BucketNames.BOOKING_APP_STORE.getStringValue(),
                        String.format("%s/%s", String.format("%s/%s", FolderNames.PROFILE_PICTURES, user.getUserId()), user.getImage()));

                // update user image
                user.setImage(null);
            }
        }

        customerRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(UserResponse.builder().message("User updated")
                        .user(StaffDTO.builder()
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

    public ResponseEntity<String> deleteCustomer(Integer id) {
        CustomerEntity user = customerRepository.findById(id).orElseThrow(() -> new ApiException("User does not exist"));
        if (user.getImage() != null) {
            storageService.delete(BucketNames.BOOKING_APP_STORE.getStringValue(),
                    String.format("%s/%s", String.format("%s/%s", FolderNames.PROFILE_PICTURES, user.getUserId()), user.getImage()));
        }

        customerRepository.delete(user);
        return ResponseEntity.status(HttpStatus.OK).body("User deleted");
    }
}
