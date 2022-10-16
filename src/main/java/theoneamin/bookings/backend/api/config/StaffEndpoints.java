package theoneamin.bookings.backend.api.config;

public class StaffEndpoints {
    public static final String USERS_STAFF = "/api/users/staff/{pageNumber}";
    public static final String USERS_STAFF_ALL = "/api/users/staff";
    public static final String STAFF_ADD = "/api/users/staff/add";
    public static final String STAFF_DELETE = "/api/users/staff/delete/{id}";
    public static final String STAFF_EDIT = "/api/users/staff/edit/{id}";
    public static final String STAFF_SEARCH = "/api/users/staff/search/{name}";
    public static final String PAGE_SIZE = "/api/users/staff/page-size";
}
