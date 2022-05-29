package theoneamin.bookings.backend.api.entity.booking;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Document(collection = "booking_slots")
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BookingSlots {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Field("seller_id")
    private Integer sellerId;

    @Field("booking_date")
    private String bookingDate;

    @Field("timeslots")
    private List<String> timeSlots;
}
