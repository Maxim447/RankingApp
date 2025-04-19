package ru.hse.rankingapp.dto.professional.records;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.hse.rankingapp.entity.enums.Gender;

import java.time.LocalTime;

/**
 * Дто с информацией о рекордах.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Дто с информацией о рекордах")
public class ProfessionalRecordsInfoDto {

    @Schema(description = "ИД записи")
    private Long id;

    @Schema(description = "Дистанция")
    private Integer distance;

    @Schema(description = "Стиль")
    private String style;

    @Schema(description = "Гендер")
    private Gender gender;

    @Schema(description = "Время")
    private LocalTime time;
}
