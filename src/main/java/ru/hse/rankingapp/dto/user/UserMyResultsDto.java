package ru.hse.rankingapp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * Дто для карточки спортсмена "Мои результаты".
 */
@Data
@Accessors(chain = true)
@Schema(description = "Дто для карточки спортсмена \"Мои результаты\"")
public class UserMyResultsDto {

    @Schema(description = "Рейтинг пользователя")
    private BigDecimal userRating;

    @Schema(description = "Соревнования")
    private List<UserCompetitionMyResultsDto> competitions;
}
