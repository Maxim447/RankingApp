package ru.hse.rankingapp.dto.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Ответ на получение данных об авторизированной организации.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Ответ на получение данных об авторизированной организации")
public class OrganizationInfoDto {

    @Schema(description = "Почта")
    private String email;

    @Schema(description = "Наименование организации")
    private String name;

    @Schema(description = "Признак открытости организации (true -> открытая, false -> закрытая)")
    private Boolean isOpen;
}
