package theoneamin.bookings.backend.api.enums;

import java.util.HashMap;
import java.util.Map;

public enum UserType {
    ADMIN(1), CUSTOMER(2), STAFF(3);

    private final int intValue;

    private static final Map<Integer, UserType> VALUES = new HashMap<>();

    static {
        for (UserType type : values()) {
            VALUES.put(type.intValue, type);
        }
    }

    UserType(int value) {
        intValue = value;
    }

    public int getIntValue() {
        return intValue;
    }

    public static UserType getUserType(int intValue) {
        return VALUES.get(intValue);
    }
}
