package theoneamin.bookings.backend.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import theoneamin.bookings.backend.api.entity.booking.BookingEntity;

import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<BookingEntity, String> {
    List<BookingEntity> findAllByStaffId(String staffId);
    List<BookingEntity> findAllByCustomerId(String customerId);
}
