package theoneamin.bookings.backend.api.entity.services;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServiceRequest {

    @NotEmpty(message = "Please provide service title")
    private String title;

    @NotNull(message = "Please provide service price")
    private BigDecimal price;

    @NotNull(message = "Please provide service min duration")
    private Integer duration;
}
