package theoneamin.bookings.backend.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

    /**
     * Get all services
     * @return list of services
     */
    public List<ServiceEntity> getAllServices() {
        return serviceRepository.findAll();
    }

    /**
     * Add service entity
     * @param serviceRequest service request
     * @return service response
     */
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

    /**
     * Edit service entity
     * @param id id of the service to edit
     * @param service service entity
     * @return service response
     */
    public ServiceResponse editService(Integer id, ServiceEntity service) {
        // find service
        ServiceEntity serviceEntity = serviceRepository.findById(id).orElseThrow(() -> new ApiException("Service does not exist in database"));

        // update fields
        BeanUtils.copyProperties(service, serviceEntity);

        // save
        ServiceEntity updated = serviceRepository.save(serviceEntity);

        // return
        return ServiceResponse
                .builder()
                .message("Service updated")
                .service(updated)
                .build();
    }

    /**
     * Delete service entity
     * @param id id of the service to delete
     */
    public void deleteService(Integer id) {
        ServiceEntity service = serviceRepository.findById(id).orElseThrow(() -> new ApiException("Service not found by id"));
        serviceRepository.delete(service);
        log.info("Service deleted");
    }
}
