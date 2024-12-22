package ru.hse.authservice.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Запрос на изменение номера телефона.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Запрос на изменение номера телефона")
public class UpdatePhoneRequestDto {

    @Schema(description = "Номер телефона")
    @NotNull(message = "Номер телефона не может быть пустым")
    @Pattern(regexp = "^\\+?[1-9]\\d{0,2} ?\\(?\\d{1,4}?\\)? ?\\d{1,4}[- ]?\\d{1,4}[- ]?\\d{1,9}$",
            message = "Неверный формат номера телефона")
    private String phone;
}
