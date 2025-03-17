package ru.hse.rankingapp.dto.user.rating;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.hse.rankingapp.entity.enums.Gender;
import ru.hse.rankingapp.enums.CategoryEnum;
import ru.hse.rankingapp.enums.ParticipantsTypeEnum;

/**
 * Поисковые параметры для таблицы рейтинга.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Поисковые параметры для таблицы рейтинга")
public class RatingSearchParamsDto {

    @Schema(description = "Любитель/Профи")
    private ParticipantsTypeEnum userType;

    @Schema(description = "Возрастная категория")
    private CategoryEnum categoryEnum;

    @Schema(description = "Пол")
    private Gender gender;

    @Schema(description = "Кол-во стартов от")
    private Long startsCountFrom;
}
