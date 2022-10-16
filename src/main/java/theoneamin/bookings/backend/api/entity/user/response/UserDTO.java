package theoneamin.bookings.backend.api.entity.user.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import theoneamin.bookings.backend.api.enums.UserType;

/**
 * We use DTO to map data from entities so that
 * we only expose what's needed
 */
@Data
@SuperBuilder
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
}
