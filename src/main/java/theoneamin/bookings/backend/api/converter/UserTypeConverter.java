package theoneamin.bookings.backend.api.converter;

import theoneamin.bookings.backend.api.enums.UserType;

import javax.persistence.AttributeConverter;

public class UserTypeConverter implements AttributeConverter<UserType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(UserType userType) {
        if (userType == null) {
            return null;
        }

        return userType.getIntValue();
    }

    @Override
    public UserType convertToEntityAttribute(Integer integer) {
        if (integer == null) {
            return null;
        }
        return UserType.getUserType(integer);
    }
}
