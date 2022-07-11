package theoneamin.bookings.backend.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import theoneamin.bookings.backend.api.entity.services.ServiceEntity;
import theoneamin.bookings.backend.api.entity.services.ServiceRequest;
import theoneamin.bookings.backend.api.entity.services.ServiceResponse;
import theoneamin.bookings.backend.api.exception.ApiException;
import theoneamin.bookings.backend.api.repository.ServiceRepository;

import java.util.List;

@Service
@Slf4j
public class ServicesUtil {
    @Autowired ServiceRepository serviceRepository;

    public List<ServiceEntity> getAllServices() {
        return serviceRepository.findAll();
    }

    public ServiceResponse addService(ServiceRequest serviceRequest) {
        // validate
        // create
        // response
        serviceRepository.findByTitle(serviceRequest.getTitle()).ifPresent(service -> {
            throw new ApiException("Service already exists");
        });

        ServiceEntity service = new ServiceEntity();
        service.setTitle(serviceRequest.getTitle());
        service.setPrice(serviceRequest.getPrice());
        service.setDuration(serviceRequest.getDuration());

        ServiceEntity saved = serviceRepository.save(service);

        return ServiceResponse
                .builder()
                .message("Service added")
                .service(saved)
                .build();
    }
}
