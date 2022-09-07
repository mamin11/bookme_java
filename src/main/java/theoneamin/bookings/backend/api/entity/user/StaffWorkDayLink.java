package theoneamin.bookings.backend.api.entity.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import theoneamin.bookings.backend.api.converter.WorkDayConverter;
import theoneamin.bookings.backend.api.enums.WorkDays;

import javax.persistence.*;

@Data
@Builder
@Entity(name = "staff_work_days")
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class StaffWorkDayLink {
    @Id
    @GeneratedValue
    private int id;

    @Column
    private Integer staffId;

    @Column
    @Convert(converter = WorkDayConverter.class)
    private WorkDays workDay;
}
