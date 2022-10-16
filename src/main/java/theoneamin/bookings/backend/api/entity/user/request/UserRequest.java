package theoneamin.bookings.backend.api.entity.user.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public abstract class UserRequest {
    @NotEmpty(message = "Please provide firstname")
    private String firstname;

    @NotEmpty(message = "Please provide lastname")
    private String lastname;

    @NotEmpty(message = "Please provide email")
    private String email;

    @NotEmpty(message = "Please provide phone number")
    private String phone;

    @NotNull(message = "Please provide user type")
    private Integer userType;

    private Integer merchantId;

    private MultipartFile image;
}
