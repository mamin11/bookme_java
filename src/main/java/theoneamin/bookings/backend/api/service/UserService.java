package theoneamin.bookings.backend.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import theoneamin.bookings.backend.api.entity.user.UserEntity;
import theoneamin.bookings.backend.api.enums.UserType;
import theoneamin.bookings.backend.api.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
public class UserService {

    @Autowired UserRepository userRepository;

    /**
     * Get all users
     * @return list of users
     */
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get all staff
     * @return list of staff users
     */
    public List<UserEntity> getAllStaff() {
        return userRepository.findAllByUserType(UserType.STAFF);
    }

    /**
     * Get all customers
     * @return list of customers
     */
    public List<UserEntity> getAllCustomers() {
        return userRepository.findAllByUserType(UserType.CUSTOMER);
    }
}
