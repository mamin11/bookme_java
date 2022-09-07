package theoneamin.bookings.backend.api.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import theoneamin.bookings.backend.api.converter.UserTypeConverter;
import theoneamin.bookings.backend.api.enums.UserType;

import javax.persistence.*;

@Data
@Entity(name = "users")
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class UserEntity {
    private static final PasswordEncoder pwEncoder = new BCryptPasswordEncoder();

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @Column
    private String email;

    @Column
    private String phone;

    @Column
    @JsonIgnore
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

    @PrePersist
    public void prePersist(){
        password = pwEncoder.encode(password);
    }

}
