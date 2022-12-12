package theoneamin.bookings.backend.api.enums;

import java.util.HashMap;
import java.util.Map;

public enum WorkHourSetting {
    CUSTOM("Custom"), DEFAULT("Default");

    private final String string;

    private static final Map<String, WorkHourSetting> VALUES = new HashMap<>();

    static {
        for (WorkHourSetting type : values()) {
            VALUES.put(type.string, type);
        }
    }

    WorkHourSetting(String value) {
        string = value;
    }

    public String getString() {
        return string;
    }

    public static WorkHourSetting getWorkHourSetting(String string) {
        return VALUES.get(string);
    }
}
