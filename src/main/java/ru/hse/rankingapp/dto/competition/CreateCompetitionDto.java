package ru.hse.rankingapp.dto.competition;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * Дто для создания соревнования.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Дто для создания соревнования")
public class CreateCompetitionDto {

    @Schema(description = "Название соревнования")
    private String competitionName;

    @Schema(description = "Местоположение")
    private String competitionLocation;

    @Schema(description = "Дата проведения соревнования")
    private LocalDate competitionDate;

    @Schema(description = "Максимальное число участников")
    private Integer maxParticipants;

    @Schema(description = "Тип соревнования")
    private String competitionType;
}
