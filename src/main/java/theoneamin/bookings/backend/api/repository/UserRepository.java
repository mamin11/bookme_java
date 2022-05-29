package theoneamin.bookings.backend.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import theoneamin.bookings.backend.api.entity.user.UserEntity;
import theoneamin.bookings.backend.api.enums.UserType;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByEmailAndUserType(String email, UserType userType);
}
