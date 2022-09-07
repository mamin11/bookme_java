package theoneamin.bookings.backend.api.entity.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity(name = "staff_timeslots")
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class StaffTimeSlotLink {
    @Id
    @GeneratedValue
    private int id;

    @Column
    private Integer staffId;

    @Column
    private String timeslot;
}
