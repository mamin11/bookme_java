package theoneamin.bookings.backend.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import theoneamin.bookings.backend.api.entity.booking.BookingEntity;

import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Integer> {
//    List<BookingEntity> findAllByStaffId(String staffId);
//    List<BookingEntity> findAllByCustomerId(String customerId);
}
