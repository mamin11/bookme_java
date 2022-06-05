package theoneamin.bookings.backend.api.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import theoneamin.bookings.backend.api.config.ServiceEndpoints;
import theoneamin.bookings.backend.api.entity.services.ServiceEntity;
import theoneamin.bookings.backend.api.service.ServicesUtil;

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
}
