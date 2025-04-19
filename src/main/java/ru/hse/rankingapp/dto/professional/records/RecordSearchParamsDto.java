package ru.hse.rankingapp.dto.professional.records;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.hse.rankingapp.entity.enums.Gender;

/**
 * Дто с поисковыми параметрами рекордов.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Дто с поисковыми параметрами рекордов")
public class RecordSearchParamsDto {

    @Schema(description = "Дистанция")
    private Integer distance;

    @Schema(description = "Стиль")
    private String style;

    @Schema(description = "Гендер")
    private Gender gender;
}
