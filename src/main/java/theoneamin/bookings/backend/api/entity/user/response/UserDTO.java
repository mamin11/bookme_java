package theoneamin.bookings.backend.api.entity.user.response;

import lombok.Builder;
import lombok.Data;;
import theoneamin.bookings.backend.api.enums.UserType;
import javax.persistence.Transient;
import java.util.List;

/**
 * We use DTO to map data from entities so that
 * we only expose what's needed
 */
@Data
@Builder
public class UserDTO {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String image;
    private UserType userType;
    private Integer allTimeBookings;
    private String fullName;
    private List<Integer> services;
    private List<Integer> working_days;
}
