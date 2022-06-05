package theoneamin.bookings.backend.api.entity.services;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@Entity(name = "services")
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class ServiceEntity {
    @Id
    private Integer id;

    @Column
    private String title;

    @Column
    private BigDecimal price;

    @Column
    private Integer duration;
}
