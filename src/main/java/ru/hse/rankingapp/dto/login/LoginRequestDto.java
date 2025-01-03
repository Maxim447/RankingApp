package ru.hse.rankingapp.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Дто запрос для входа.
 */
@Schema(description = "Запрос для входа")
@Data
public class LoginRequestDto {

    @Schema(description = "Логин")
    @NotNull(message = "Логин не может быть пустым")
    private String login;

    @Schema(description = "Пароль")
    @NotNull(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 256, message = "Длина пароля должна быть в интервале от 8 до 256 символов")
    private String password;
}