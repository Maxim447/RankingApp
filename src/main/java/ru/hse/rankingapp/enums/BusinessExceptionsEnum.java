package ru.hse.rankingapp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Enum с бизнес-ошибками.
 */
@Getter
@AllArgsConstructor
public enum BusinessExceptionsEnum {

    //user already exist
    EMAIL_ALREADY_EXISTS("Пользователь с такой электронной почтой уже существует", HttpStatus.CONFLICT),
    PHONE_ALREADY_EXISTS("Пользователь с таким номером телефона уже существует", HttpStatus.CONFLICT),

    //user not found
    USER_NOT_FOUND_BY_PHONE("Пользователь с таким номером телефона не найден", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND_BY_EMAIL("Пользователь с такой электронной почтой не найден", HttpStatus.NOT_FOUND),

    //field is empty
    LOGIN_IS_EMPTY("Электронная почта или телефон не могут быть пустыми", HttpStatus.BAD_REQUEST),
    WRONG_LOGIN_FORMAT("Неверный формат телефона или электронной почты", HttpStatus.BAD_REQUEST),

    //password
    PASSWORD_NOT_EQUALS_CONFIRM_PASSWORD("Пароль не совпадает с паролем подтверждения", HttpStatus.BAD_REQUEST),
    WRONG_OLD_PASSWORD("Неверный старый пароль", HttpStatus.BAD_REQUEST),
    NEW_PASSWORD_EQUALS_OLD_PASSWORD("Новый пароль не совпадать со старым", HttpStatus.BAD_REQUEST),

    //authentication
    MISSING_AUTH_HEADER("Отсутствует заголовок авторизации", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED_ACCESS("Неавторизованный доступ к приложению", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED("Срок действия токена истек", HttpStatus.UNAUTHORIZED),

    //user authentication
    USER_NOT_ENABLED("Пользователь недоступен", HttpStatus.FORBIDDEN),
    USER_EXPIRED("Срок действия пользователя истек", HttpStatus.FORBIDDEN),
    USER_DELETED("Пользователь был удален", HttpStatus.FORBIDDEN),

    //organization
    ORGANIZATION_DELETED("Организация была удалена", HttpStatus.FORBIDDEN),
    NOT_ENOUGH_RULES("Вы не можете совершить данное действие", HttpStatus.CONFLICT),
    ORGANIZATION_NOT_FOUND_BY_EMAIL("Организация с такой электронной почтой не найдена", HttpStatus.NOT_FOUND),

    //personal data
    PERSONAL_DATA_ALREADY_EXISTS("Персональные данные уже были отправлены",HttpStatus.CONFLICT),

    PERSONAL_DATA_NOT_FOUND("Персональные данные не найдены",HttpStatus.NOT_FOUND),

    CANNOT_SEND_MESSAGE("Не удалось отправить сообщение на почту",HttpStatus.NOT_FOUND);

    private final String message;

    private final HttpStatus status;
}