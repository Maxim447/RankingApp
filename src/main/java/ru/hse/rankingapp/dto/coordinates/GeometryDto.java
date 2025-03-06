package ru.hse.rankingapp.dto.coordinates;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Дто геометрии.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Дто геометрии")
public class GeometryDto {

    @Schema(description = "Тип геометрии")
    private String type;

    @Schema(description = "Координаты")
    private List<Double> coordinates;
}
