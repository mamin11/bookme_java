package theoneamin.bookings.backend.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import theoneamin.bookings.backend.api.aws.StorageService;
import theoneamin.bookings.backend.api.entity.user.*;
import theoneamin.bookings.backend.api.entity.user.request.*;
import theoneamin.bookings.backend.api.entity.user.response.StaffDTO;
import theoneamin.bookings.backend.api.entity.user.response.UserResponse;
import theoneamin.bookings.backend.api.enums.BucketNames;
import theoneamin.bookings.backend.api.enums.FolderNames;
import theoneamin.bookings.backend.api.enums.UserType;
import theoneamin.bookings.backend.api.enums.WorkDays;
import theoneamin.bookings.backend.api.exception.ApiException;
import theoneamin.bookings.backend.api.repository.*;
import theoneamin.bookings.backend.api.utility.UtilityService;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StaffService {

    public static final int PAGE_SIZE = 5;
    @Autowired StaffRepository staffRepository;
    @Autowired StaffServiceRepository staffServiceRepository;
    @Autowired StaffWorkDayRepository staffWorkDayRepository;
    @Autowired StaffWorkHourRepository staffWorkHourRepository;
    @Autowired StorageService storageService;
    @Autowired UtilityService utilityService;


    /**
     * Get all customers
     * @return list of staff
     * @param userPageRequest page number and size
     */
    public List<StaffDTO> getPageStaff(UserPageRequest userPageRequest) {
        Page<StaffEntity> userEntityList = staffRepository.findAllByUserType(UserType.STAFF, PageRequest.of(userPageRequest.getPageNumber(), userPageRequest.getPageSize()));
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
                .workingHoursChoice(userEntity.getWorkingHoursSetting() != null ? userEntity.getWorkingHoursSetting().getString() : null)
                .workingHours(userEntity.getWorkingHours().stream().map(workHourEntity -> {
                    StaffWorkHoursDTO workHoursDTO = new StaffWorkHoursDTO();
                    workHoursDTO.setWorkDay(workHourEntity.getWorkDay().getIntValue());
                    workHoursDTO.setWorkHours(List.of(workHourEntity.getStartTime().toString(), workHourEntity.getEndTime().toString()));

                    return workHoursDTO;
                }).collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
    }

    /**
     * Get all customers
     * @return list of staff
     */
    public List<StaffDTO> getAllStaff() {
        List<StaffEntity> userEntityList = staffRepository.findAllByUserType(UserType.STAFF);
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
                .workingHoursChoice(userEntity.getWorkingHoursSetting() != null ? userEntity.getWorkingHoursSetting().getString() : null)
                .workingHours(userEntity.getWorkingHours().stream().map(workHourEntity -> {
                    StaffWorkHoursDTO workHoursDTO = new StaffWorkHoursDTO();
                    workHoursDTO.setWorkDay(workHourEntity.getWorkDay().getIntValue());
                    workHoursDTO.setWorkHours(List.of(workHourEntity.getStartTime().toString(), workHourEntity.getEndTime().toString()));

                    return workHoursDTO;
                }).collect(Collectors.toList()))
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
    public Integer getMaxPageSize(Integer pageSize){
        return (int) Math.ceil((double) staffRepository.countByUserType(UserType.STAFF)/pageSize);
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

        // save working-hours
        if (createUserRequest.getWorkHoursChoice().equals("Custom")) {
            if (createUserRequest.getWorkingHours().isEmpty()) {
                log.error("Custom work hours selected but no data provided. Work hours in payload: {}", createUserRequest.getWorkingHours());
                throw new ApiException("Custom work hours selected but no data provided");
            }

            // get work-day-hours from request
            for (StaffWorkHoursDTO workHour : createUserRequest.getWorkingHours()) {
                List<LocalTime> sortedHours = workHour.getWorkHours()
                        .stream()
                        .map(LocalTime::parse)
                        .sorted(LocalTime::compareTo)
                        .collect(Collectors.toList());

                WorkHourEntity workHourEntity = new WorkHourEntity();
                workHourEntity.setStaff(savedStaff);
                workHourEntity.setStartTime(sortedHours.get(0));
                workHourEntity.setEndTime(sortedHours.get(sortedHours.size()-1));
                workHourEntity.setWorkDay(WorkDays.getWorkDay(workHour.getWorkDay()));
                workHourEntity.setTenantId(1);//todo: use each tenant's id

                staffWorkHourRepository.save(workHourEntity);
            }

            log.info("Saved working hours: {} for staff: {}", createUserRequest.getWorkingHours(), savedStaff.getUserId());
        } else {
            // use default business for the tenant
            // TODO: create tenant table and get this data from there

        }

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

        // handle working hours changes
        if (editUserRequest.getWorkHoursChoice().equals("Custom")) {
            handleStaffWorkHoursUpdate(user, editUserRequest, workdaysInDbNotInRequest);
        }

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
     * Update staff working hours
     * @param user staff
     * @param editUserRequest request
     * @param removedWorkDays work days that were requested to be removed
     */
    private void handleStaffWorkHoursUpdate(StaffEntity user, EditStaffRequest editUserRequest, List<WorkDays> removedWorkDays) {
        List<WorkHourEntity> existingWorkHours = user.getWorkingHours();
        List<StaffWorkHoursDTO> requestedWorkHours = editUserRequest.getWorkingHours();

        // delete any work hours belonging to removed days
        existingWorkHours.stream()
                .filter(workHourEntity -> removedWorkDays.contains(workHourEntity.getWorkDay()))
                .forEach(workHourEntity -> staffWorkHourRepository.delete(workHourEntity));

        List<WorkHourEntity> workDaysToMaintain = existingWorkHours.stream()
                .filter(workHourEntity -> !removedWorkDays.contains(workHourEntity.getWorkDay()))
                .collect(Collectors.toList());

        // to add: work days in request but not in db
        List<StaffWorkHoursDTO> workDaysInRequestNotDb = editUserRequest.getWorkingHours()
                .stream().filter(staffWorkHoursDTO -> !existingWorkHours
                        .stream().map(WorkHourEntity::getWorkDay).collect(Collectors.toList())
                        .contains(WorkDays.getWorkDay(staffWorkHoursDTO.getWorkDay())))
                .collect(Collectors.toList());

        log.info("Adding: {} working hours for staff id: {}", workDaysInRequestNotDb, user.getUserId());

        for (StaffWorkHoursDTO workHoursDTO : workDaysInRequestNotDb) {
            List<LocalTime> sortedTimes = workHoursDTO.getWorkHours().stream()
                    .map(LocalTime::parse)
                    .sorted(LocalTime::compareTo)
                    .collect(Collectors.toList());

            WorkHourEntity workHourEntity = new WorkHourEntity();
            workHourEntity.setStaff(user);
            workHourEntity.setStartTime(sortedTimes.get(0));
            workHourEntity.setEndTime(sortedTimes.get(sortedTimes.size()-1));
            workHourEntity.setWorkDay(WorkDays.getWorkDay(workHoursDTO.getWorkDay()));
            workHourEntity.setTenantId(1);//todo: use each tenant's id

            staffWorkHourRepository.save(workHourEntity);
        }

//        List<WorkDays> requestedWorkDaysList = requestedWorkHours.stream().map(StaffWorkHoursDTO::getWorkDay).map(WorkDays::getWorkDay).collect(Collectors.toList());
//        List<WorkDays> workDaysToMaintainList = workDaysToMaintain.stream().map(WorkHourEntity::getWorkDay).collect(Collectors.toList());
//        if (!requestedWorkDaysList.containsAll(workDaysToMaintainList)) {
//            String error = String.format("Requested work days %s does not match expected work days %s for the staff", requestedWorkDaysList, workDaysToMaintainList);
//            log.error(error);
//            throw new ApiException(error);
//        }

        for (StaffWorkHoursDTO requestedWorkHour : requestedWorkHours) {
            Optional<WorkHourEntity> optionalWorkHourEntity = workDaysToMaintain
                    .stream().filter(workHourEntity -> requestedWorkHour.getWorkDay().equals(workHourEntity.getWorkDay().getIntValue())).findAny();

            if (optionalWorkHourEntity.isEmpty()) {
                String message = String.format("Work day %s could not be found in expected work-day %s for staff", requestedWorkHour, workDaysToMaintain);
                log.error(message);
                throw new ApiException(message);
            }

            WorkHourEntity matchedWorkHourEntity = optionalWorkHourEntity.get();

            // sort requested hours
            List<LocalTime> sortedTimes = requestedWorkHour.getWorkHours().stream()
                    .map(LocalTime::parse)
                    .sorted(LocalTime::compareTo)
                    .collect(Collectors.toList());

            // compare start and end times
            if (!matchedWorkHourEntity.getStartTime().equals(sortedTimes.get(0))) {
                log.info("Updating staff id: {} work start time to: {} on: {}", user.getUserId(), sortedTimes.get(0), matchedWorkHourEntity.getWorkDay());
                matchedWorkHourEntity.setStartTime(sortedTimes.get(0));
            }

            if (!matchedWorkHourEntity.getEndTime().equals(sortedTimes.get(sortedTimes.size()-1))) {
                log.info("Updating staff id: {} work end time to: {} on: {}", user.getUserId(), sortedTimes.get(sortedTimes.size()-1), matchedWorkHourEntity.getWorkDay());
                matchedWorkHourEntity.setEndTime(sortedTimes.get(sortedTimes.size()-1));
            }

            staffWorkHourRepository.save(matchedWorkHourEntity);

        }

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
