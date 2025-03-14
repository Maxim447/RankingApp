package ru.hse.rankingapp.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.text.RandomStringGenerator;

/**
 * Класс для генерации паролей.
 */
@UtilityClass
public class PasswordGenerator {

    /**
     * Сгенерировать пароль.
     *
     * @param length длина
     * @return пароль
     */
    public String generatePassword(int length) {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(Character::isLetterOrDigit)
                .get();

        return generator.generate(length);
    }
}
