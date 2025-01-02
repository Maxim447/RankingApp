package ru.hse.authservice.dto.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Дто запроса для регистрации.
 */
@Data
@Schema(description = "Запрос для регистрации")
public class SignUpOrganizationRequestDto {

    @Schema(description = "Электронная почта")
    @NotNull(message = "Электронная почта не может быть пустым")
    @Email(message = "Неверный формат электронной почты")
    private String organizationEmail;

    @Schema(description = "Наименование организации")
    @NotNull(message = "Наименование организации не может быть пустым")
    private String organizationName;

    @Schema(description = "Пароль")
    @NotNull(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 256, message = "Длина пароля должна быть в интервале от 8 до 256 символов")
    private String password;

    @Schema(description = "Пароль подтверждения")
    @NotNull(message = "Пароль подтверждения не может быть пустым")
    @Size(min = 8, max = 256, message = "Длина пароля подтверждения должна быть в интервале от 8 до 256 символов")
    private String confirmPassword;
}
