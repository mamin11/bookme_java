package theoneamin.bookings.backend.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import theoneamin.bookings.backend.api.entity.user.*;
import theoneamin.bookings.backend.api.enums.UserType;
import theoneamin.bookings.backend.api.enums.WorkDays;
import theoneamin.bookings.backend.api.exception.ApiException;
import theoneamin.bookings.backend.api.repository.StaffServiceRepository;
import theoneamin.bookings.backend.api.repository.StaffTimeSlotRepository;
import theoneamin.bookings.backend.api.repository.StaffWorkDayRepository;
import theoneamin.bookings.backend.api.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class StaffService {

    @Autowired UserRepository userRepository;
    @Autowired StaffServiceRepository staffServiceRepository;
    @Autowired StaffTimeSlotRepository staffTimeSlotRepository;
    @Autowired StaffWorkDayRepository staffWorkDayRepository;


    /**
     * Get all customers
     * @return list of staff
     */
    public List<UserEntity> getAllStaff() {
        return userRepository.findAllByUserType(UserType.STAFF);
    }

    /**
     * Add staff to the database
     * @param userRequest payload
     * @return added user and message
     */
    @Transactional
    public ResponseEntity<UserResponse> addStaff(UserRequest userRequest) {
        //validate email not taken
        userRepository.findByEmail(userRequest.getEmail()).ifPresent(userEntity -> {throw new ApiException("Email already taken");});

        // upload image and get link
        //todo: upload image

        // create user
        UserEntity user = new UserEntity();
        user.setFirstname(userRequest.getFirstname());
        user.setLastname(userRequest.getLastname());
        user.setEmail(userRequest.getEmail());
        user.setUserType(UserType.getUserType(userRequest.getUserType()));
        user.setPassword(userRequest.getPassword());
        user.setPhone(userRequest.getPhone());

        UserEntity savedStaff = userRepository.save(user);
        log.info("Saved staff: {}", savedStaff);

        // create staff-services link
        //todo: validate service exist
        userRequest.getServices().forEach(service -> {
            StaffServicesLink staffServicesLink = StaffServicesLink.builder()
                    .staffId(savedStaff.getId())
                    .serviceId(service)
                    .build();

            staffServiceRepository.save(staffServicesLink);
        });
        log.info("Saved services: {} for staff: {}", userRequest.getServices(), savedStaff.getFullName());

        // create staff-work-days
        List<WorkDays> savedWorkDays = new ArrayList<>();
        userRequest.getWorkingDays().forEach(workday -> {
            StaffWorkDayLink staffWorkDayLink = StaffWorkDayLink.builder()
                    .staffId(savedStaff.getId())
                    .workDay(WorkDays.getWorkDay(workday))
                    .build();

            savedWorkDays.add(staffWorkDayRepository.save(staffWorkDayLink).getWorkDay());
        });
        log.info("Saved working days: {} for staff: {}", savedWorkDays, savedStaff.getFullName());

        return ResponseEntity.status(HttpStatus.OK)
                .body(UserResponse.builder()
                        .message("successfully added user")
                        .user(savedStaff)
                        .build());
    }

    /**
     * Get user by email
     * @param email email address
     * @return user response
     */
    public ResponseEntity<UserResponse> getStaffByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException("User does not exist"));
        return ResponseEntity.status(HttpStatus.OK)
                .body(UserResponse.builder()
                        .message("success")
                        .user(user)
                        .build());
    }

    /**
     * Edit user details
     * @param userRequest payload
     * @return user response
     */
    public ResponseEntity<UserResponse> editStaff(UserRequest userRequest) {
        // validate staff email
        UserEntity user = userRepository.findByEmail(userRequest.getEmail()).orElseThrow(() -> new ApiException("User does not exist"));
        BeanUtils.copyProperties(userRequest, user);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK)
                .body(UserResponse.builder().message("User updated")
                        .user(user)
                        .build());
    }

    public ResponseEntity<String> deleteStaff(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException("User does not exist"));
        userRepository.delete(user);

        return ResponseEntity.status(HttpStatus.OK).body("User deleted");
    }
}
