package theoneamin.bookings.backend.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import theoneamin.bookings.backend.api.entity.booking.BookingEntity;

@Repository
public interface BookingRepository extends MongoRepository<BookingEntity, Long> {
}
