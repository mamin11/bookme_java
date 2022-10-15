package theoneamin.bookings.backend.api.enums;

import java.util.HashMap;
import java.util.Map;

public enum FolderNames {
    PROFILE_PICTURES("profile-pictures"), BOOKING_CONTENT("booking-content");

    private final String stringValue;

    private static final Map<String, FolderNames> VALUES = new HashMap<>();

    static {
        for (FolderNames type : values()) {
            VALUES.put(type.stringValue, type);
        }
    }

    FolderNames(String value) {
        stringValue = value;
    }

    public String getStringValue() {
        return stringValue;
    }

    public static FolderNames getFolder(String stringValue) {
        return VALUES.get(stringValue);
    }
}
