package theoneamin.bookings.backend.api.entity.booking;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming
public class BookingDTO {
    private String bookingId;
    private String customerEmail;
    private LocalDate date;
    private List<String> timeSlots;
}
