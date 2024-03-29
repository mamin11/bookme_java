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

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity(name = "staff_work_days")
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class StaffWorkDayLink extends DateAudited {
    @Id
    @GeneratedValue
    private int id;

    @Column
    @Convert(converter = WorkDayConverter.class)
    private WorkDays workDay;

    @JsonIgnore
    @JoinColumn(name="staffId", referencedColumnName = "user_id")
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = StaffEntity.class)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private StaffEntity staff;
}
