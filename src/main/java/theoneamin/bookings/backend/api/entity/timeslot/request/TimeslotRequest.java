package theoneamin.bookings.backend.api.entity.timeslot.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TimeslotRequest {

    @NotNull(message = "Please provide staff")
    private Integer staffId;

    @NotNull(message = "Please provide booking date")
    private String bookingDate;
}
