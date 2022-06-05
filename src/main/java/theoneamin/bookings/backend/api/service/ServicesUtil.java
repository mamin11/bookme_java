package theoneamin.bookings.backend.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import theoneamin.bookings.backend.api.entity.services.ServiceEntity;
import theoneamin.bookings.backend.api.repository.ServiceRepository;

import java.util.List;

@Service
@Slf4j
public class ServicesUtil {
    @Autowired ServiceRepository serviceRepository;

    public List<ServiceEntity> getAllServices() {
        return serviceRepository.findAll();
    }
}
