package theoneamin.bookings.backend.api.enums;

import java.util.HashMap;
import java.util.Map;

public enum WorkDays {
    MONDAY(1), TUESDAY(2), WEDNESDAY(3), THURSDAY(4), FRIDAY(5), SATURDAY(6), SUNDAY(7);

    private final int intValue;

    private static final Map<Integer, WorkDays> VALUES = new HashMap<>();

    static {
        for (WorkDays type : values()) {
            VALUES.put(type.intValue, type);
        }
    }

    WorkDays(int value) {
        intValue = value;
    }

    public int getIntValue() {
        return intValue;
    }

    public static WorkDays getWorkDay(int intValue) {
        return VALUES.get(intValue);
    }
}
