package theoneamin.bookings.backend.api.entity.booking;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BookingRequest {
    @NotNull(message = "Please provide customer id")
    private Integer customerId;

    @NotNull(message = "Please provide staff id")
    private Integer staffId;

    @NotNull(message = "Please provide service id")
    private Integer serviceId;

    @NotNull(message = "Please provide booking date")
    private LocalDate bookingDate;

    @NotNull(message = "Please provide at least one time slot")
    private List<String> bookingSlots;

    private boolean notifyCustomer;
}