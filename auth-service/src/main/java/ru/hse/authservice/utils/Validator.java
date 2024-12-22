package ru.hse.authservice.utils;

import lombok.experimental.UtilityClass;
import ru.hse.commonmodule.enums.BusinessExceptionsEnum;
import ru.hse.commonmodule.enums.RegexpEnum;
import ru.hse.commonmodule.exception.BusinessException;

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
