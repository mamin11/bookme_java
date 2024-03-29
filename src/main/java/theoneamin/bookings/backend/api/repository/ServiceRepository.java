package theoneamin.bookings.backend.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import theoneamin.bookings.backend.api.entity.services.ServiceEntity;

import java.util.Optional;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Integer> {
    Optional<ServiceEntity> findByTitle(String title);
}
