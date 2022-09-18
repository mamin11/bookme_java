package theoneamin.bookings.backend.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import theoneamin.bookings.backend.api.entity.user.StaffServicesLink;

import java.util.List;

@Repository
public interface StaffServiceRepository extends JpaRepository<StaffServicesLink, Integer> {
//    List<StaffServicesLink> findAllByStaffId(Integer id);
    void deleteByServiceIdIn(List<Integer> ids);
//    void deleteByStaffId(Integer staffId);
}
