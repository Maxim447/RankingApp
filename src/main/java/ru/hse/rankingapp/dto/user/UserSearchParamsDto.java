package ru.hse.rankingapp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Поисковые параметры пользователя.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Поисковые параметры пользователя")
public class UserSearchParamsDto {

    @Schema(description = "Имя")
    private String firstName;

    @Schema(description = "Фамилия")
    private String lastName;

    @Schema(description = "Отчество")
    private String middleName;

    @Schema(description = "Почта")
    private String email;
}
