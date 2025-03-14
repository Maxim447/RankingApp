package ru.hse.rankingapp.dto.curator;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.hse.rankingapp.entity.enums.Gender;

import java.time.LocalDate;

/**
 * Дто для создания пользователя, через куратора.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Дто для создания пользователя, через куратора")
public class CuratorUserCreateDto {

    @Schema(description = "Электронная почта")
    @NotNull(message = "Электронная почта не может быть пустым")
    @Email(message = "Неверный формат электронной почты")
    private String email;

    @Schema(description = "Номер телефона (личный)")
    @Pattern(regexp = "^\\+?[1-9]\\d{0,2} ?\\(?\\d{1,4}?\\)? ?\\d{1,4}[- ]?\\d{1,4}[- ]?\\d{1,9}$",
            message = "Неверный формат номера телефона")
    private String userPhone;

    @Schema(description = "Номер телефона (экстренный)")
    @NotNull(message = "Экстренный номер телефона не может быть пустым")
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

    @Schema(description = "Дата рождения")
    private LocalDate birthDate;

    @Schema(description = "Пол (MALE/FEMALE)")
    private Gender gender;

    @Schema(description = "Пароль")
    @Size(min = 8, max = 256, message = "Длина пароля должна быть в интервале от 8 до 256 символов")
    private String password;

    @Schema(description = "Пароль подтверждения")
    @Size(min = 8, max = 256, message = "Длина пароля подтверждения должна быть в интервале от 8 до 256 символов")
    private String confirmPassword;

    @Schema(description = "Нужно ли сгенерировать пароль")
    private Boolean isNeedGeneratePassword;
}
