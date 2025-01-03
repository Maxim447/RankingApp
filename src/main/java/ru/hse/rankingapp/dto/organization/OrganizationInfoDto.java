package ru.hse.rankingapp.dto.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.hse.rankingapp.entity.enums.Role;

/**
 * Ответ на получение данных об авторизированной организации.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Ответ на получение данных об авторизированной организации")
public class OrganizationInfoDto {

    @Schema(description = "Id пользователя")
    private Long id;

    @Schema(description = "Почта")
    private String email;

    @Schema(description = "Наименование организации")
    private String name;

    @Schema(description = "Роль пользователя")
    private Role role;
}
