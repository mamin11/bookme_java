package theoneamin.bookings.backend.api.enums;

import java.util.HashMap;
import java.util.Map;

public enum WorkHourSetting {
    DEFAULT(0), CUSTOM(1);

    private final Integer integer;

    private static final Map<Integer, WorkHourSetting> VALUES = new HashMap<>();

    static {
        for (WorkHourSetting type : values()) {
            VALUES.put(type.integer, type);
        }
    }

    WorkHourSetting(Integer value) {
        integer = value;
    }

    public Integer getInteger() {
        return integer;
    }

    public static WorkHourSetting getWorkHourSetting(Integer integer) {
        return VALUES.get(integer);
    }
}
