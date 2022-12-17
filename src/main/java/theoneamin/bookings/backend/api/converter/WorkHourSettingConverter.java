package theoneamin.bookings.backend.api.converter;

import theoneamin.bookings.backend.api.enums.WorkHourSetting;

import javax.persistence.AttributeConverter;

public class WorkHourSettingConverter implements AttributeConverter<WorkHourSetting, Integer> {
    @Override
    public Integer convertToDatabaseColumn(WorkHourSetting workHourSetting) {
        if (workHourSetting == null) {
            return null;
        }

        return workHourSetting.getInteger();
    }

    @Override
    public WorkHourSetting convertToEntityAttribute(Integer integer) {
        if (integer == null) {
            return null;
        }
        return WorkHourSetting.getWorkHourSetting(integer);
    }
}
