package theoneamin.bookings.backend.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import theoneamin.bookings.backend.api.entity.user.StaffWorkDayLink;
import theoneamin.bookings.backend.api.entity.user.WorkHourEntity;
import theoneamin.bookings.backend.api.enums.WorkDays;

import java.util.List;

@Repository
public interface StaffWorkHourRepository extends JpaRepository<WorkHourEntity, Integer> {
}
