package theoneamin.bookings.backend.api.entity.booking;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.List;

@Document(collection = "booking")
@Data
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
    private LocalDate bookingDate;

    @Field
    private List<String> bookingSlots;

    @Field
    private boolean notifyCustomer;
}
