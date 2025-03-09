package ru.hse.rankingapp.dto.coordinates;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Дто ответа с geoJson'ом.
 */
@Data
@Accessors(chain = true)
@Schema(description = "GeoJson")
public class SimpleGeoJsonResponseDto {

    @Schema(description = "ИД записи в бд")
    private Long id;

    @Schema(description = "Geo Json")
    private GeoJson geoJson;
}
