package theoneamin.bookings.backend.api.converter;

import theoneamin.bookings.backend.api.enums.WorkDays;
import javax.persistence.AttributeConverter;

public class WorkDayConverter implements AttributeConverter<WorkDays, Integer> {
    @Override
    public Integer convertToDatabaseColumn(WorkDays workDays) {
        if (workDays == null) {
            return null;
        }

        return workDays.getIntValue();
    }

    @Override
    public WorkDays convertToEntityAttribute(Integer integer) {
        if (integer == null) {
            return null;
        }
        return WorkDays.getWorkDay(integer);
    }
}
