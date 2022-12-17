package theoneamin.bookings.backend.api.entity.user.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import theoneamin.bookings.backend.api.entity.user.request.StaffWorkHoursDTO;

import java.util.List;

;

/**
 * We use DTO to map data from entities so that
 * we only expose what's needed
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class StaffDTO extends UserDTO {
    private List<Integer> services;
    private List<Integer> working_days;
    private Integer workingHoursChoice;
    private List<StaffWorkHoursDTO> workingHours;
}
