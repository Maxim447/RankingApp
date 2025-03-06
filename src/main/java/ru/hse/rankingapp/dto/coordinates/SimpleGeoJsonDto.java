package ru.hse.rankingapp.dto.coordinates;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Дто для сохранения координат организации.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Дто для сохранения координат организации")
public class SimpleGeoJsonDto {

    @Schema(description = "Наименование")
    private String name;

    @Schema(description = "Описание")
    private String description;

    @Schema(description = "Геометрия")
    private GeometryDto geometry;

    @Schema(description = "Email организации")
    private String email;
}
