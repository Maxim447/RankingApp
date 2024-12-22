package ru.hse.authservice.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import ru.hse.commonmodule.exception.BusinessExceptionsHandler;

/**
 * Класс для обработки ошибок.
 */
@ControllerAdvice
public class ExceptionsHandler extends BusinessExceptionsHandler {

    public ExceptionsHandler() {
        super();
    }
}

