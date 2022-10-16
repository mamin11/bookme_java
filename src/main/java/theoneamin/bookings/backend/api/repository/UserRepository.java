package theoneamin.bookings.backend.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import theoneamin.bookings.backend.api.entity.user.UserEntity;
import theoneamin.bookings.backend.api.enums.UserType;

import java.util.List;
import java.util.Optional;

public interface UserRepository<T extends UserEntity> {
    Optional<T> findByEmailAndUserType(String email, UserType userType);
    Optional<T> findByEmail(String email);
    List<T> findAllByUserType(UserType userType);
    Page<T> findAllByUserType(UserType userType, Pageable pageable);
    Integer countByUserType(UserType userType);
    List<T> findByUserTypeAndFirstnameContainsOrLastnameContains(UserType userType, String param1, String param2);
}
