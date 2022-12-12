package theoneamin.bookings.backend.api.converter;

import theoneamin.bookings.backend.api.enums.WorkHourSetting;

import javax.persistence.AttributeConverter;

public class WorkHourSettingConverter implements AttributeConverter<WorkHourSetting, String> {
    @Override
    public String convertToDatabaseColumn(WorkHourSetting workHourSetting) {
        if (workHourSetting == null) {
            return null;
        }

        return workHourSetting.getString();
    }

    @Override
    public WorkHourSetting convertToEntityAttribute(String string) {
        if (string == null) {
            return null;
        }
        return WorkHourSetting.getWorkHourSetting(string);
    }
}
