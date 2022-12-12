package theoneamin.bookings.backend.api.entity.user.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@EqualsAndHashCode(callSuper = true)
public class CreateStaffRequest extends CreateUserRequest{
    @NotNull(message = "Please provide at least 1 service")
    private List<Integer> services;

    @NotNull(message = "Please provide working days")
    private List<Integer> workingDays;

    @NotEmpty(message = "Please choose either default or custom working hours")
    private String workHoursChoice;

    private List<StaffWorkHoursDTO> workingHours;

    // todo: refactor and remove workingDays and use workingHours to get both days and hours
}
