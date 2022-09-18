package theoneamin.bookings.backend.api.entity.booking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import theoneamin.bookings.backend.api.entity.user.StaffEntity;
import theoneamin.bookings.backend.api.entity.util.DateAudited;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@Entity(name = "timeslots")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class StaffTimeslot extends DateAudited {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "booking_date")
    private LocalDate bookingDate;

    @Column(name = "booking_time")
    private LocalTime bookingTime;

    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name="staffId", referencedColumnName = "user_id")
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = StaffEntity.class)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private StaffEntity staff;
}
