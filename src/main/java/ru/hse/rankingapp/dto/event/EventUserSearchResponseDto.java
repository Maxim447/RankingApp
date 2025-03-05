package ru.hse.rankingapp.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EventUserSearchResponseDto {

    @Schema(description = "ФИО")
    private String fullName;

    @Schema(description = "ПОЛ")
    private String gender;

    @Schema(description = "Возраст")
    private Long age;

    @Schema(description = "Рейтинг")
    private Double rating;

    @Schema(description = "Категория")
    private String category;
}
