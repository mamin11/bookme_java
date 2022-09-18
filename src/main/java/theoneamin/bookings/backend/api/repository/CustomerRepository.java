package theoneamin.bookings.backend.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import theoneamin.bookings.backend.api.entity.user.CustomerEntity;

@Repository
public interface CustomerRepository extends UserRepository<CustomerEntity>, JpaRepository<CustomerEntity, Integer> {
}
