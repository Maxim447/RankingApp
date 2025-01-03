package ru.hse.rankingapp.exception.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.hse.rankingapp.exception.dto.ValidationExceptionResponseDto;
import ru.hse.rankingapp.exception.BusinessException;
import ru.hse.rankingapp.exception.dto.ErrorMessageDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс для обработки ошибок.
 */
@ControllerAdvice
public class BusinessExceptionsHandler extends ResponseEntityExceptionHandler {

    private static final String DEFAULT_ERROR_MESSAGE = "Непредвиденная ошибка";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException exception,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        List<ValidationExceptionResponseDto> validationExceptions = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(ex -> ValidationExceptionResponseDto.of(
                        ex.getField(),
                        ex.getDefaultMessage()
                ))
                .toList();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(validationExceptions);
    }

    /**
     * Обработка бизнес ошибки.
     *
     * @param request request
     * @param ex      ошибка
     * @return Дто бизнес ошибки.
     */
    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<ErrorMessageDto> handleBusinessException(WebRequest request, Exception ex) {
        ErrorMessageDto errorMessageDto = buildErrorMessageDto(request, ex);
        return new ResponseEntity<>(errorMessageDto, new HttpHeaders(), errorMessageDto.getStatus());
    }

    private ErrorMessageDto buildErrorMessageDto(WebRequest request, Exception ex) {
        return new ErrorMessageDto()
                .setPath(request.getDescription(false))
                .setTimestamp(LocalDateTime.now().toString())
                .setMsg(determineErrorMessage(ex))
                .setStatus(determineHttpStatus(ex).value());
    }

    private HttpStatusCode determineHttpStatus(Exception ex) {
        if (ex instanceof BusinessException) {
            return ((BusinessException) ex).getStatus();
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    private String determineErrorMessage(Exception ex) {
        if (ex instanceof BusinessException) {
            return ex.getMessage();
        }

        return DEFAULT_ERROR_MESSAGE;
    }
}

