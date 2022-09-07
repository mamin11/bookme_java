package theoneamin.bookings.backend.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import theoneamin.bookings.backend.api.entity.user.StaffWorkDayLink;

import javax.persistence.criteria.CriteriaBuilder;

public interface StaffWorkDayRepository extends JpaRepository<StaffWorkDayLink, CriteriaBuilder.In> {
}
