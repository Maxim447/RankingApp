package ru.hse.authservice.utils;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;

/**
 * Класс для генерации кодов подтверждения.
 */
@UtilityClass
public class VerificationCodeGenerator {

    private final String CHARACTERS = "0123456789";

    private final int CODE_LENGTH = 6;

    private final SecureRandom RANDOM = new SecureRandom();

    /**
     * Сгенерировать код подтверждения.
     *
     * @return код подтверждения
     */
    public String generateVerificationCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }
}