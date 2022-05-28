package theoneamin.bookings.backend.api.entity.booking;

import lombok.*;

@Data
@Builder
public class BookingResponse {
    private String message;
    private BookingDTO booking;
}
