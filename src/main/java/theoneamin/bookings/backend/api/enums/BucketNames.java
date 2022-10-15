package theoneamin.bookings.backend.api.enums;

import java.util.HashMap;
import java.util.Map;

public enum BucketNames {
    BOOKING_APP_STORE("booking-app-store");

    private final String stringValue;

    private static final Map<String, BucketNames> VALUES = new HashMap<>();

    static {
        for (BucketNames type : values()) {
            VALUES.put(type.stringValue, type);
        }
    }

    BucketNames(String value) {
        stringValue = value;
    }

    public String getStringValue() {
        return stringValue;
    }

    public static BucketNames getBucket(String stringValue) {
        return VALUES.get(stringValue);
    }
}
