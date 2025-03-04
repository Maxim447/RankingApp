package ru.hse.rankingapp.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.hse.rankingapp.entity.enums.Gender;
import ru.hse.rankingapp.enums.CategoryEnum;

@Data
@Accessors(chain = true)
@Schema(description = "Дто для поиска участников в рамках заплыва")
public class EventUserSearchRequestDto {

    @Schema(description = "Пол участника")
    private Gender gender;

    @Schema(description = "Возраст участника")
    private Integer age;

    @Schema(description = "Категория возраста через \"-\" (Пример: 18-21)")
    private CategoryEnum category;
}
