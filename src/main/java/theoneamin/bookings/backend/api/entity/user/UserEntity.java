package theoneamin.bookings.backend.api.entity.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import theoneamin.bookings.backend.api.converter.UserTypeConverter;
import theoneamin.bookings.backend.api.enums.UserType;

import javax.persistence.*;

@Data
@Entity(name = "users")
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class UserEntity {
    @Id
    private Integer id;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String image;

    @Column
    @Convert(converter = UserTypeConverter.class)
    private UserType userType;

    @Transient
    private String fullName;

    public String getFullName() {
        return this.firstname+" "+this.lastname;
    }
}
