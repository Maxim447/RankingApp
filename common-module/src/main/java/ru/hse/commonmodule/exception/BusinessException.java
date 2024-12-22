package ru.hse.commonmodule.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import ru.hse.commonmodule.enums.BusinessExceptionsEnum;

/**
 * Класс бизнес ошибки.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {

    private final HttpStatus status;

    public BusinessException(BusinessExceptionsEnum businessExceptionsEnum) {
        super(businessExceptionsEnum.getMessage());
        this.status = businessExceptionsEnum.getStatus();
    }

    public BusinessException(String message, HttpStatus httpStatus) {
        super(message);
        this.status = httpStatus;
    }
}
