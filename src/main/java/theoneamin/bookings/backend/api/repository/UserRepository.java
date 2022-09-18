package theoneamin.bookings.backend.api.repository;

import theoneamin.bookings.backend.api.entity.user.UserEntity;
import theoneamin.bookings.backend.api.enums.UserType;

import java.util.List;
import java.util.Optional;

public interface UserRepository<T extends UserEntity> {
    Optional<T> findByEmailAndUserType(String email, UserType userType);
    Optional<T> findByEmail(String email);
    List<T> findAllByUserType(UserType userType);
}
