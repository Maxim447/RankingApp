package ru.hse.authservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.hse.commonmodule.enums.Role;

/**
 * Ответ на получение данных об авторизированном пользователе.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Ответ на получение данных об авторизированном пользователе")
public class UserInfoDto {

    @Schema(description = "Id пользователя")
    private Long id;

    @Schema(description = "Почта")
    private String email;

    @Schema(description = "Телефон")
    private String phone;

    @Schema(description = "Имя")
    private String firstName;

    @Schema(description = "Фамилия")
    private String lastName;

    @Schema(description = "Отчество")
    private String middleName;

    @Schema(description = "Роль пользователя")
    private Role role;
}
