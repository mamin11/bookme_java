package theoneamin.bookings.backend.api.entity.booking;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BookingRequest {
    @NotEmpty(message = "Please provide customer id")
    private String customerEmail;

    @NotEmpty(message = "Please provide staff id")
    private String staffEmail;

    @NotNull(message = "Please provide service id")
    private Integer serviceId;

    @NotNull(message = "Please provide booking date")
    private String bookingDate;

    @NotNull(message = "Please provide at least one time slot")
    private List<String> bookingSlots;

    private boolean notifyCustomer;
}