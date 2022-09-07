package theoneamin.bookings.backend.api.entity.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import theoneamin.bookings.backend.api.enums.UserType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserRequest {
    @NotEmpty(message = "Please provide firstname")
    private String firstname;

    @NotEmpty(message = "Please provide lastname")
    private String lastname;

    @NotEmpty(message = "Please provide email")
    private String email;

    @NotEmpty(message = "Please provide password")
    @Length(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotEmpty(message = "Please provide phone number")
    private String phone;

    @NotNull(message = "Please provide user type")
    private Integer userType;

    @NotNull(message = "Please provide at least 1 service")
    private List<Integer> services;

    @NotNull(message = "Please provide working days")
    private List<Integer> workingDays;

    private Integer merchantId;

}
