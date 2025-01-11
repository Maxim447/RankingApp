package ru.hse.rankingapp.dto.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Параметры поиска организации.
 */
@Data
@Schema(description = "Параметры поиска организации")
public class OrganizationSearchParamsDto {

    @Schema(description = "Наименование организации (поиск по вхождению)")
    private String name;

    @Schema(description = "Почта")
    private String email;
}
