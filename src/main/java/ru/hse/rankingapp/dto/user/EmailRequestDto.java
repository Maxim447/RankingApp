package ru.hse.rankingapp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Запрос на изменение электронной почты.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Запрос на изменение электронной почты")
public class EmailRequestDto {

    @Schema(description = "Электронная почта")
    @NotNull(message = "Электронная почта не может быть пустым")
    @Email(message = "Неверный формат электронной почты")
    private String email;
}
