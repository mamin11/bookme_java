package theoneamin.bookings.backend.api.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import theoneamin.bookings.backend.api.aws.StorageService;
import theoneamin.bookings.backend.api.entity.user.*;
import theoneamin.bookings.backend.api.entity.user.request.CreateStaffRequest;
import theoneamin.bookings.backend.api.entity.user.request.CreateUserRequest;
import theoneamin.bookings.backend.api.entity.user.request.EditStaffRequest;
import theoneamin.bookings.backend.api.entity.user.request.EditUserRequest;
import theoneamin.bookings.backend.api.entity.user.response.StaffDTO;
import theoneamin.bookings.backend.api.entity.user.response.UserResponse;
import theoneamin.bookings.backend.api.enums.BucketNames;
import theoneamin.bookings.backend.api.enums.FolderNames;
import theoneamin.bookings.backend.api.enums.UserType;
import theoneamin.bookings.backend.api.enums.WorkDays;
import theoneamin.bookings.backend.api.exception.ApiException;
import theoneamin.bookings.backend.api.repository.*;
import theoneamin.bookings.backend.api.utility.UtilityService;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StaffService {

    public static final int PAGE_SIZE = 5;
    @Autowired StaffRepository staffRepository;
    @Autowired StaffServiceRepository staffServiceRepository;
    @Autowired StaffWorkDayRepository staffWorkDayRepository;
    @Autowired StorageService storageService;
    @Autowired UtilityService utilityService;


    /**
     * Get all customers
     * @return list of staff
     * @param pageNumber page number
     */
    public List<StaffDTO> getAllStaff(Integer pageNumber) {
        Page<StaffEntity> userEntityList = staffRepository.findAllByUserType(UserType.STAFF, PageRequest.of(pageNumber, PAGE_SIZE));
        return userEntityList.stream().map(userEntity -> StaffDTO.builder()
                .id(userEntity.getUserId())
                .firstname(userEntity.getFirstname())
                .lastname(userEntity.getLastname())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .image(utilityService.getImageLink(userEntity))
                .userType(userEntity.getUserType())
                .allTimeBookings(0)
                .fullName(userEntity.getFirstname()+" "+userEntity.getLastname())
                .services(userEntity.getServicesLinks().stream().map(StaffServicesLink::getServiceId).collect(Collectors.toList()))
                .working_days(userEntity.getWorkDayLinks().stream().map(StaffWorkDayLink::getWorkDay).map(WorkDays::getIntValue).collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
    }

    /**
     * Get all customers
     * @return list of staff
     * @param name staff name
     */
    public List<StaffDTO> searchStaff(String name) {
        List<StaffEntity> userEntityList = staffRepository.findByUserTypeAndFirstnameContainsOrUserTypeAndLastnameContains(UserType.STAFF, name, UserType.STAFF, name);
        return userEntityList.stream().map(userEntity -> StaffDTO.builder()
                .id(userEntity.getUserId())
                .firstname(userEntity.getFirstname())
                .lastname(userEntity.getLastname())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .image(utilityService.getImageLink(userEntity))
                .userType(userEntity.getUserType())
                .allTimeBookings(0)
                .fullName(userEntity.getFirstname()+" "+userEntity.getLastname())
                .services(userEntity.getServicesLinks().stream().map(StaffServicesLink::getServiceId).collect(Collectors.toList()))
                .working_days(userEntity.getWorkDayLinks().stream().map(StaffWorkDayLink::getWorkDay).map(WorkDays::getIntValue).collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
    }

    /**
     * Get max page size for staff users from the database
     * @return max page size
     */
    public Integer getMaxPageSize(){
        return (int) Math.ceil((double) staffRepository.countByUserType(UserType.STAFF)/PAGE_SIZE);
    }

    /**
     * Add staff to the database
     * @param createUserRequest payload
     * @return added user and message
     */
    @Transactional
    public ResponseEntity<UserResponse> addStaff(CreateStaffRequest createUserRequest) {
        //validate email not taken
        staffRepository.findByEmail(createUserRequest.getEmail()).ifPresent(userEntity -> {throw new ApiException("Email already taken");});


        // create user
        StaffEntity user = new StaffEntity();
        user.setFirstname(createUserRequest.getFirstname());
        user.setLastname(createUserRequest.getLastname());
        user.setEmail(createUserRequest.getEmail());
        user.setUserType(UserType.getUserType(createUserRequest.getUserType()));
        user.setPassword(createUserRequest.getPassword());
        user.setPhone(createUserRequest.getPhone());

        StaffEntity savedStaff = staffRepository.save(user);
        log.info("Saved staff: {}", savedStaff);

        if (createUserRequest.getImage() != null) {
            // upload image and get link
            String filename = utilityService.handleImageUpload(savedStaff, createUserRequest.getImage());

            // update user image
            savedStaff.setImage(filename);
            staffRepository.saveAndFlush(savedStaff);
        }

        // create staff-services link
        createUserRequest.getServices().forEach(service -> {
            StaffServicesLink staffServicesLink = new StaffServicesLink();
            staffServicesLink.setStaff(user);
            staffServicesLink.setServiceId(service);

            staffServiceRepository.save(staffServicesLink);
        });
        log.info("Saved services: {} for staff: {}", createUserRequest.getServices(), savedStaff.getUserId());

        // create staff-work-days
        List<WorkDays> savedWorkDays = new ArrayList<>();
        createUserRequest.getWorkingDays().forEach(workday -> {
            StaffWorkDayLink staffWorkDayLink = StaffWorkDayLink.builder()
                    .staff(user)
                    .workDay(WorkDays.getWorkDay(workday))
                    .build();

            savedWorkDays.add(staffWorkDayRepository.save(staffWorkDayLink).getWorkDay());
        });
        log.info("Saved working days: {} for staff: {}", savedWorkDays, savedStaff.getUserId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(UserResponse.builder()
                        .message("successfully added user")
                        .user(StaffDTO.builder()
                                .id(savedStaff.getUserId())
                                .firstname(savedStaff.getFirstname())
                                .lastname(savedStaff.getLastname())
                                .email(savedStaff.getEmail())
                                .phone(savedStaff.getPhone())
                                .image(savedStaff.getImage())
                                .userType(savedStaff.getUserType())
                                .allTimeBookings(0)
                                .fullName(savedStaff.getFirstname()+" "+savedStaff.getLastname())
                                .build())
                        .build());
    }

    /**
     * Get user by email
     * @param email email address
     * @return user response
     */
    public ResponseEntity<UserResponse> getStaffByEmail(String email) {
        StaffEntity user = staffRepository.findByEmail(email).orElseThrow(() -> new ApiException("User does not exist"));
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
                                .fullName(user.getFirstname()+" "+user.getLastname())
                                .build())
                        .build());
    }

    /**
     * Edit user details
     * @param editUserRequest payload
     * @return user response
     */
    @Transactional
    public ResponseEntity<UserResponse> editStaff(Integer id, EditStaffRequest editUserRequest) {
        // validate staff email
        StaffEntity user = staffRepository.findById(id).orElseThrow(() -> new ApiException("User does not exist"));
        BeanUtils.copyProperties(editUserRequest, user);

        List<StaffServicesLink> userServicesInDB = user.getServicesLinks();
        List<Integer> userRequestServices = editUserRequest.getServices();

        if (editUserRequest.getImage() != null) {
            // upload image and get link
            String filename = utilityService.handleImageUpload(user, editUserRequest.getImage());

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

        // service-links to remove
        List<Integer> servicesInDbNotInRequest = userServicesInDB.stream()
                .map(StaffServicesLink::getServiceId)
                .filter(integer -> !userRequestServices.contains(integer))
                .collect(Collectors.toList());
        log.info("Services in DB for user: {} that are not in request: {}", user.getUserId(), servicesInDbNotInRequest);

        // service-links to add
        List<Integer> servicesInRequestNotInDb = userRequestServices.stream()
                .filter(integer -> !userServicesInDB
                        .stream().map(StaffServicesLink::getServiceId).collect(Collectors.toList())
                        .contains(integer))
                .collect(Collectors.toList());
        log.info("Services in request for user: {} that are not in DB: {}", user.getUserId(), servicesInRequestNotInDb);

        handleStaffServicesUpdate(user, servicesInDbNotInRequest, servicesInRequestNotInDb);

        // workday-links to remove
        List<WorkDays> workdaysInDbNotInRequest = user.getWorkDayLinks().stream()
                .map(StaffWorkDayLink::getWorkDay).map(WorkDays::getIntValue)
                .filter(integer -> !editUserRequest.getWorkingDays().contains(integer))
                .map(WorkDays::getWorkDay)
                .collect(Collectors.toList());
        log.info("Work-days in DB for user: {} that are not in request: {}", user.getUserId(), workdaysInDbNotInRequest);

        // workday-links to add
        List<Integer> workdaysInRequestNotInDb = editUserRequest
                .getWorkingDays().stream()
                .filter(integer -> !user.getWorkDayLinks()
                        .stream().map(StaffWorkDayLink::getWorkDay).map(WorkDays::getIntValue).collect(Collectors.toList())
                        .contains(integer))
                .collect(Collectors.toList());
        log.info("Work-days in request for user: {} that are not in DB: {}", user.getUserId(), workdaysInRequestNotInDb);

        handleStaffWorkDaysUpdate(user, workdaysInDbNotInRequest, workdaysInRequestNotInDb);

        staffRepository.save(user);

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
                                .fullName(user.getFirstname()+" "+user.getLastname())
                                .build())
                        .build());
    }


    /**
     * Create new staff-workday links
     * Delete no longer-needed links
     * @param user user entity
     * @param workdaysInDbNotInRequest workday-links to delete
     * @param workdaysInRequestNotInDb workday-links to create
     */
    private void handleStaffWorkDaysUpdate(StaffEntity user, List<WorkDays> workdaysInDbNotInRequest, List<Integer> workdaysInRequestNotInDb) {
        // create work-day link
        workdaysInRequestNotInDb.forEach(workday -> {
            log.info("adding new work-day: {} for user: {}", workday, user.getUserId());
            staffWorkDayRepository.save(StaffWorkDayLink.builder()
                    .workDay(WorkDays.getWorkDay(workday))
                    .staff(user)
                    .build());
        });

        // delete staff services from DB
        staffWorkDayRepository.deleteByWorkDayIn(workdaysInDbNotInRequest);
    }

    /**
     * Create new staff-service links
     * Delete no longer-needed staff-service links
     * @param user user entity
     * @param servicesInDbNotInRequest services-links to delete
     * @param servicesInRequestNotInDb service-links to create
     */
    private void handleStaffServicesUpdate(StaffEntity user, List<Integer> servicesInDbNotInRequest, List<Integer> servicesInRequestNotInDb) {
        // create staff-services link
        servicesInRequestNotInDb.forEach(serviceId -> {
            log.info("adding new service: {} for user: {}", serviceId, user.getUserId());
            StaffServicesLink staffServicesLink = new StaffServicesLink();
            staffServicesLink.setStaff(user);
            staffServicesLink.setServiceId(serviceId);
            staffServiceRepository.save(staffServicesLink);
        });

        // delete staff services from DB
        staffServiceRepository.deleteByServiceIdIn(servicesInDbNotInRequest);
    }

    @Transactional
    public void deleteStaff(Integer id) {
        StaffEntity user = staffRepository.findById(id).orElseThrow(() -> new ApiException("User does not exist"));
        if (user.getImage() != null) {
            storageService.delete(BucketNames.BOOKING_APP_STORE.getStringValue(),
                    String.format("%s/%s", String.format("%s/%s", FolderNames.PROFILE_PICTURES, user.getUserId()), user.getImage()));
        }
        staffRepository.delete(user);
        log.info("Staff and related links deleted");
    }
}
