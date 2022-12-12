package theoneamin.bookings.backend.api.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import theoneamin.bookings.backend.api.converter.WorkDayConverter;
import theoneamin.bookings.backend.api.entity.util.DateAudited;
import theoneamin.bookings.backend.api.enums.WorkDays;

import javax.persistence.*;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity(name = "working_hours")
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class WorkHourEntity extends DateAudited {
    @Id
    @GeneratedValue
    private int id;

    @Column
    private LocalTime startTime;

    @Column
    private LocalTime endTime;

    @Column
    @Convert(converter = WorkDayConverter.class)
    private WorkDays workDay;

    @Column
    private Integer tenantId;

    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name="staffId", referencedColumnName = "user_id")
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = StaffEntity.class)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private StaffEntity staff;
}
