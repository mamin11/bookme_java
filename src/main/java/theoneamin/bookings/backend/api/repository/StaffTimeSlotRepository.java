package theoneamin.bookings.backend.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import theoneamin.bookings.backend.api.entity.user.StaffTimeSlotLink;

public interface StaffTimeSlotRepository extends JpaRepository<StaffTimeSlotLink, Integer> {
}
