package theoneamin.bookings.backend.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import theoneamin.bookings.backend.api.entity.booking.StaffTimeslot;
import theoneamin.bookings.backend.api.entity.user.StaffEntity;
import theoneamin.bookings.backend.api.enums.WorkDays;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface StaffTimeslotRepository extends JpaRepository<StaffTimeslot, Integer> {
    List<StaffTimeslot> findByStaffAndBookingDate(StaffEntity staff, LocalDate bookingDate);
    void deleteByStaffAndBookingDateAndBookingTimeIn(StaffEntity staff, LocalDate bookingDate, List<LocalTime> bookingTimes);
}
