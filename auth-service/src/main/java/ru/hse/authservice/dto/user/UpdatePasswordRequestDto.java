package ru.hse.authservice.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Запрос на изменение пароля.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Запрос на изменение пароля")
public class UpdatePasswordRequestDto {

    @Schema(description = "Старый пароль")
    @NotNull(message = "Старый пароль не может быть пустым")
    @Size(min = 8, max = 256, message = "Длина пароля должна быть в интервале от 8 до 256 символов")
    private String oldPassword;

    @Schema(description = "Новый пароль")
    @NotNull(message = "Новый пароль не может быть пустым")
    @Size(min = 8, max = 256, message = "Длина пароля должна быть в интервале от 8 до 256 символов")
    private String newPassword;
}
