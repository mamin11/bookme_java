package theoneamin.bookings.backend.api.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import theoneamin.bookings.backend.api.converter.UserTypeConverter;
import theoneamin.bookings.backend.api.entity.util.DateAudited;
import theoneamin.bookings.backend.api.enums.UserType;

import javax.persistence.*;
import java.io.Serializable;

@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public abstract class UserEntity extends DateAudited implements Serializable {
    private static final PasswordEncoder pwEncoder = new BCryptPasswordEncoder();

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "user_id")
    private Integer userId;

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

    @PrePersist
    public void prePersist(){
        password = pwEncoder.encode(password);
    }
}
