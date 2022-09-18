package theoneamin.bookings.backend.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import theoneamin.bookings.backend.api.entity.user.StaffEntity;

@Repository
public interface StaffRepository extends UserRepository<StaffEntity>, JpaRepository<StaffEntity, Integer> {
}
