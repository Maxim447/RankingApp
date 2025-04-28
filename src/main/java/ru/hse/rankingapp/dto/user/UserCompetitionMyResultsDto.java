package ru.hse.rankingapp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 *  Дто для отображения соревнований в карточке "Мои результаты".
 */
@Data
@Accessors(chain = true)
@Schema(description = "Дто для отображения соревнований в карточке \"Мои результаты\"")
public class UserCompetitionMyResultsDto {

    @Schema(description = "Название соревнование")
    private String competitionName;

    @Schema(description = "Список заплывов")
    private List<UserEventMyResultsDto> events;
}
