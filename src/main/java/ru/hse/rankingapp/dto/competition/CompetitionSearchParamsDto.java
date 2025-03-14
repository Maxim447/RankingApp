package ru.hse.rankingapp.dto.competition;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.hse.rankingapp.entity.enums.StatusEnum;

import java.time.LocalDate;

/**
 * Дто с поисковыми параметрами соревнований.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Дто с поисковыми параметрами соревнований")
public class CompetitionSearchParamsDto {

    @Schema(description = "Название соревнования (поиск по вхождению)")
    private String name;

    @Schema(description = "Местоположение соревнования (поиск по вхождению)")
    private String location;

    @Schema(description = "Дата соревнования")
    private LocalDate date;

    @Schema(description = "Максимальное число участников")
    private Integer maxParticipants;

    @Schema(description = "Минимальное число участников")
    private Integer minParticipants;

    @Schema(description = "Статус соревнования")
    private StatusEnum status;

    @Schema(description = "Тип соревнования (поиск по вхождению)")
    private String competitionType;
}
