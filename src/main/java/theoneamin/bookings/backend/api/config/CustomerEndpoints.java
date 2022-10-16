package theoneamin.bookings.backend.api.config;

public class CustomerEndpoints {
    public static final String USERS_CUSTOMER = "/api/users/customers/{pageNumber}";
    public static final String USERS_CUSTOMER_ALL = "/api/users/customers";
    public static final String CUSTOMER_ADD = "/api/users/customers/add";
    public static final String CUSTOMER_GET = "/api/users/customers/get";
    public static final String CUSTOMER_EDIT = "/api/users/customers/edit/{id}";
    public static final String CUSTOMER_DELETE = "/api/users/customers/delete/{id}";
    public static final String CUSTOMER_SEARCH = "/api/users/customers/search/{name}";
    public static final String PAGE_SIZE = "/api/users/customers/page-size";
}
