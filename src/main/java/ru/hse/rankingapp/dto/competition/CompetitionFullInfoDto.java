package ru.hse.rankingapp.dto.competition;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.hse.rankingapp.dto.event.EventInfoDto;
import ru.hse.rankingapp.dto.organization.OrganizationInfoDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Дто с полной информацией о соревновании.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Дто с полной информацией о соревновании")
public class CompetitionFullInfoDto {

    @Schema(description = "Название соревнования")
    private String name;

    @Schema(description = "Местоположение соревнования")
    private String location;

    @Schema(description = "Дата соревнования")
    private LocalDate date;

    @Schema(description = "Максимальное число участников")
    private Integer maxParticipants;

    @Schema(description = "Тип соревнования")
    private String competitionType;

    @Schema(description = "Юид соревнования")
    private UUID competitionUuid;

    @Schema(description = "Информация о заплывах")
    private List<EventInfoDto> events;

    @Schema(description = "Информация об организаторе")
    private OrganizationInfoDto organizationInfo;
}
