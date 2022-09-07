package theoneamin.bookings.backend.api.entity.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Builder
@Entity(name = "staff_service")
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class StaffServicesLink {
    @Id
    @GeneratedValue
    private int id;

    @Column
    private Integer staffId;

    @Column
    private Integer serviceId;
}
