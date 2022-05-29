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

@Document(collection = "booking")
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Field
    private Integer customerId;

    @Field
    private Integer serviceId;

    @Field
    private Integer staffId;

    @Field
    private String bookingDate;

    @Field
    private List<String> bookingSlots;

    @Field
    private boolean notifyCustomer;
}
