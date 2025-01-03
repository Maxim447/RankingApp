package ru.hse.rankingapp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Дто запроса для регистрации.
 */
@Data
@Schema(description = "Запрос для регистрации")
public class SignUpUserRequestDto {

    @Schema(description = "Электронная почта")
    @NotNull(message = "Электронная почта не может быть пустым")
    @Email(message = "Неверный формат электронной почты")
    private String email;

    @Schema(description = "Номер телефона")
    @NotNull(message = "Номер телефона не может быть пустым")
    @Pattern(regexp = "^\\+?[1-9]\\d{0,2} ?\\(?\\d{1,4}?\\)? ?\\d{1,4}[- ]?\\d{1,4}[- ]?\\d{1,9}$",
            message = "Неверный формат номера телефона")
    private String emergencyPhone;

    @Schema(description = "Имя")
    @NotNull(message = "Имя должно быть заполнено")
    private String firstName;

    @Schema(description = "Фамилия")
    @NotNull(message = "Фамилия должна быть заполнена")
    private String lastName;

    @Schema(description = "Отчество")
    private String middleName;

    @Schema(description = "Возраст")
    private Integer age;

    @Schema(description = "Пароль")
    @NotNull(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 256, message = "Длина пароля должна быть в интервале от 8 до 256 символов")
    private String password;

    @Schema(description = "Пароль подтверждения")
    @NotNull(message = "Пароль подтверждения не может быть пустым")
    @Size(min = 8, max = 256, message = "Длина пароля подтверждения должна быть в интервале от 8 до 256 символов")
    private String confirmPassword;
}
