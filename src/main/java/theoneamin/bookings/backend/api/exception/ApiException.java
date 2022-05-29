package theoneamin.bookings.backend.api.exception;

public class ApiException extends RuntimeException{
    public ApiException(String errorMessage) {
        super(errorMessage);
    }
}
