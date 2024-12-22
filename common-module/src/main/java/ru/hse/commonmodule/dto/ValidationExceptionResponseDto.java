package ru.hse.commonmodule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Дто ошибки валидации.
 */
@Schema(description = "Ошибка валидации")
@Data
@AllArgsConstructor(staticName = "of")
public class ValidationExceptionResponseDto {

    @Schema(description = "Поле, непрошедшее валидацию")
    private String field;

    @Schema(description = "Сообщение")
    private String message;
}
