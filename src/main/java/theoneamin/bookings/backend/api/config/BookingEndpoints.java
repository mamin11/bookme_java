package theoneamin.bookings.backend.api.config;

public class BookingEndpoints {
    public static final String TEST = "/api/test";
    public static final String BOOKING_ADD = "/api/booking/add";
    public static final String BOOKING_BY_ID = "/api/booking/get/{id}";
    public static final String BOOKING_BY_STAFF = "/api/booking/staff/{id}";
    public static final String BOOKING_BY_CUSTOMER = "/api/booking/customer/{id}";
    public static final String BOOKING_BY_SERVICE = "/api/booking/service/{id}";
    public static final String BOOKING_BY_DATE = "/api/booking/date/{date}";
    public static final String BOOKING_ALL = "/api/booking/all";
    public static final String BOOKING_EDIT = "/api/booking/edit/{id}";
    public static final String BOOKING_DELETE = "/api/booking/delete/{id}";
}
