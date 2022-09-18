package theoneamin.bookings.backend.api.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import theoneamin.bookings.backend.api.entity.util.DateAudited;

import javax.persistence.*;


@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@Entity(name = "staff_service")
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class StaffServicesLink extends DateAudited {
    @Id
    @GeneratedValue
    private int id;

    @Column
    private Integer serviceId;

    @JsonIgnore
    @JoinColumn(name="staffId", referencedColumnName = "user_id")
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = StaffEntity.class)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private StaffEntity staff;
}
