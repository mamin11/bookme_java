package theoneamin.bookings.backend.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import theoneamin.bookings.backend.api.entity.user.StaffEntity;
import theoneamin.bookings.backend.api.entity.user.WorkHourEntity;

@Repository
public interface StaffWorkHourRepository extends JpaRepository<WorkHourEntity, Integer> {
    void deleteByStaff(StaffEntity staff);
}
