package theoneamin.bookings.backend.api.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import theoneamin.bookings.backend.api.config.ServiceEndpoints;
import theoneamin.bookings.backend.api.entity.services.ServiceEntity;
import theoneamin.bookings.backend.api.entity.services.ServiceRequest;
import theoneamin.bookings.backend.api.entity.services.ServiceResponse;
import theoneamin.bookings.backend.api.service.ServicesUtil;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping()
@CrossOrigin(origins = "*")
@Slf4j
@Validated
public class ServiceController {
    @Autowired ServicesUtil servicesUtil;

    @GetMapping(ServiceEndpoints.SERVICE_ALL)
    public ResponseEntity<List<ServiceEntity>> getAllServices() {
        log.info("{} request", ServiceEndpoints.SERVICE_ALL);
        return ResponseEntity.status(HttpStatus.OK).body(servicesUtil.getAllServices());
    }

    @PostMapping(ServiceEndpoints.SERVICE_ADD)
    public ResponseEntity<ServiceResponse> addService(@Valid @RequestBody ServiceRequest serviceRequest) {
        log.info("{} request: {}", ServiceEndpoints.SERVICE_ADD, serviceRequest);
        ServiceResponse response = servicesUtil.addService(serviceRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
