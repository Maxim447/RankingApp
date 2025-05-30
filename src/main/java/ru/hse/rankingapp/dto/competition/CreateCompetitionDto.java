package ru.hse.rankingapp.dto.competition;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.hse.rankingapp.dto.event.CreateEventDto;
import ru.hse.rankingapp.enums.ParticipantsTypeEnum;

import java.time.LocalDate;
import java.util.List;

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

    @Schema(description = "Ссылка на трансляцию (Например, ВК видео)")
    private String videoLink;

    @Schema(description = "Заплывы соревнования")
    private List<CreateEventDto> events;
}
