package ru.hse.rankingapp.dto.coordinates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.locationtech.jts.geom.Geometry;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class CoordinateProjectionDto {

    private Long id;

    private String name;

    private String description;

    private Geometry geometry;
}
