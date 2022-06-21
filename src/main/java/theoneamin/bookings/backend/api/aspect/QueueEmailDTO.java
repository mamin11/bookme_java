package theoneamin.bookings.backend.api.aspect;

import lombok.Builder;
import lombok.Data;
import theoneamin.bookings.backend.api.entity.booking.BookingDTO;

import java.util.UUID;

@Data
@Builder
public class QueueEmailDTO {
    private String job;
    private UUID uuid = UUID.randomUUID();
    private BookingDTO data;
}
