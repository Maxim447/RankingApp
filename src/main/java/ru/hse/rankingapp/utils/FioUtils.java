package ru.hse.rankingapp.utils;

import lombok.experimental.UtilityClass;
import ru.hse.rankingapp.entity.UserEntity;
import ru.hse.rankingapp.enums.SeparatorEnum;

/**
 * Утилитный класс для ФИО.
 */
@UtilityClass
public class FioUtils {

    /**
     * Получить ФИО в виде 1 строки.
     */
    public String buildFullName(String firstName, String lastName, String middleName) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(lastName).append(SeparatorEnum.SPACE.getValue());
        stringBuilder.append(firstName).append(SeparatorEnum.SPACE.getValue());

        if (middleName != null) {
            stringBuilder.append(middleName);
        }

        return stringBuilder.toString();
    }

    /**
     * Получить ФИО в виде 1 строки.
     */
    public String buildFullName(UserEntity user) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(user.getLastName()).append(SeparatorEnum.SPACE.getValue());
        stringBuilder.append(user.getFirstName()).append(SeparatorEnum.SPACE.getValue());

        String middleName = user.getMiddleName();
        if (middleName != null) {
            stringBuilder.append(middleName);
        }

        return stringBuilder.toString();
    }
}
