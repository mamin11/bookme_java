package theoneamin.bookings.backend.api.entity.user.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserPageRequest {
    @NotNull(message = "Please provide page number")
    private Integer pageSize;

    @NotNull(message = "Please provide page size")
    private Integer pageNumber;
}
