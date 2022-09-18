package theoneamin.bookings.backend.api.entity.user.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotEmpty;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@EqualsAndHashCode(callSuper = true)
public class CreateUserRequest extends UserRequest{

    @NotEmpty(message = "Please provide password")
    @Length(min = 6, message = "Password must be at least 6 characters")
    private String password;

}
