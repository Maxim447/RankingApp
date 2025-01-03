package ru.hse.rankingapp.utils;

import lombok.experimental.UtilityClass;
import ru.hse.rankingapp.enums.BusinessExceptionsEnum;
import ru.hse.rankingapp.enums.RegexpEnum;
import ru.hse.rankingapp.exception.BusinessException;

/**
 * Класс для валидации.
 */
@UtilityClass
public class Validator {

    /**
     * Валидация логина.
     *
     * @param login логин
     */
    public void validateLogin(String login) {
        if (login == null || login.trim().isEmpty()) {
            throw new BusinessException(BusinessExceptionsEnum.LOGIN_IS_EMPTY);
        }

        if (!(login.matches(RegexpEnum.EMAIL_PATTERN.getPattern()) ||
                login.matches(RegexpEnum.PHONE_PATTERN.getPattern()))) {
            throw new BusinessException(BusinessExceptionsEnum.WRONG_LOGIN_FORMAT);
        }
    }
}
