package theoneamin.bookings.backend.api.entity.user.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import theoneamin.bookings.backend.api.entity.user.CustomerEntity;

import java.util.List;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CustomerResponse {
    private String message;
    private List<CustomerEntity> customers;
}
