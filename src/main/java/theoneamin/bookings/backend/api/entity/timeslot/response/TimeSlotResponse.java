package theoneamin.bookings.backend.api.entity.timeslot.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import theoneamin.bookings.backend.api.entity.timeslot.StaffTimeslot;

import java.util.List;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TimeSlotResponse {
    private String message;
    private List<StaffTimeslot> timeslot;
}
