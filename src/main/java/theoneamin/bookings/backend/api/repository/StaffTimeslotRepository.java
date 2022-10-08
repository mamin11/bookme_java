package theoneamin.bookings.backend.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import theoneamin.bookings.backend.api.entity.timeslot.StaffTimeslot;
import theoneamin.bookings.backend.api.entity.user.StaffEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface StaffTimeslotRepository extends JpaRepository<StaffTimeslot, Integer> {
    List<StaffTimeslot> findByStaffAndBookingDate(StaffEntity staff, LocalDate bookingDate);
    void deleteByStaffAndBookingDateAndBookingTimeIn(StaffEntity staff, LocalDate bookingDate, List<LocalTime> bookingTimes);
    List<StaffTimeslot> findByStaffAndBookingDateAndBookingTimeIn(StaffEntity staff, LocalDate bookingDate, List<LocalTime> bookingTimes);
}
