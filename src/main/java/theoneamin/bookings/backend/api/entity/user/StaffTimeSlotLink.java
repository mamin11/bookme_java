package theoneamin.bookings.backend.api.entity.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import theoneamin.bookings.backend.api.entity.util.DateAudited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "staff_timeslots")
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class StaffTimeSlotLink extends DateAudited {
    @Id
    @GeneratedValue
    private int id;

    @Column
    private Integer staffId;

    @Column
    private String timeslot;
}
