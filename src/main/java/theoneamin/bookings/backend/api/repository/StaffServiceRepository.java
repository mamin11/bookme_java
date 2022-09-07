package theoneamin.bookings.backend.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import theoneamin.bookings.backend.api.entity.user.StaffServicesLink;

public interface StaffServiceRepository extends JpaRepository<StaffServicesLink, Integer> {
}
