package ru.hse.rankingapp.dto.competition;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.hse.rankingapp.enums.ParticipantsTypeEnum;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Дто с информацией о соревновании.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Дто с информацией о соревновании")
public class CompetitionInfoDto {

    @Schema(description = "Название соревнования")
    private String name;

    @Schema(description = "Местоположение соревнования")
    private String location;

    @Schema(description = "Дата соревнования")
    private LocalDate date;

    @Schema(description = "Описание соревнования")
    private String description;

    @Schema(description = "Поле для связи с организатором")
    private String contactLink;

    @Schema(description = "Поле для связи с организатором")
    private String contactLink2;

    @Schema(description = "Поле для связи с организатором")
    private String contactLink3;

    @Schema(description = "Тип участников")
    private ParticipantsTypeEnum participantsType;

    @Schema(description = "Тип соревнования")
    private String competitionType;

    @Schema(description = "Юид соревнования")
    private UUID competitionUuid;
}
