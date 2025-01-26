package ru.hse.rankingapp.dto.password;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

/**
 * Дто для восстановления пароля.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Дто для восстановления пароля")
public class PasswordChangeDto {

    @Schema(description = "Пароль")
    @NotNull(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 256, message = "Длина пароля должна быть в интервале от 8 до 256 символов")
    private String password;

    @Schema(description = "Пароль подтверждения")
    @NotNull(message = "Пароль подтверждения не может быть пустым")
    @Size(min = 8, max = 256, message = "Длина пароля подтверждения должна быть в интервале от 8 до 256 символов")
    private String confirmPassword;

    @Schema(description = "Токен на смену пароля")
    private UUID token;
}
