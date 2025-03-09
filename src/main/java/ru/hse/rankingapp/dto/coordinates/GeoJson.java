package ru.hse.rankingapp.dto.coordinates;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "GeoJson")
public class GeoJson {

    @Schema(description = "Дто геометрии")
    private GeometryDto geometry;

    @Schema(description = "Дто пропертей")
    private PropertiesDto properties;
}
