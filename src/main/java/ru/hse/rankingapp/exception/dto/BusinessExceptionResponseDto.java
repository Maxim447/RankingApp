package ru.hse.rankingapp.exception.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Дто бизнес ошибки.
 */
@Schema(description = "Бизнес ошибка")
@Data
@AllArgsConstructor(staticName = "of")
public class BusinessExceptionResponseDto {

    @Schema(description = "Сообщение")
    private String message;
}