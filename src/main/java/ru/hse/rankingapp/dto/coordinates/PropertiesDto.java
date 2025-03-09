package ru.hse.rankingapp.dto.coordinates;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Проперти geoJons'а.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Проперти geoJons'а")
public class PropertiesDto {

    @Schema(description = "Наименование")
    private String name;

    @Schema(description = "Описание")
    private String description;
}
