package theoneamin.bookings.backend.api.entity.booking;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BookingDTO {
    private String bookingId;
    private String customerFirstName;
    private String customerEmail;
    private String staffEmail;
    private String date;
    private boolean notifyCustomer;
    private List<String> timeslots;
}
