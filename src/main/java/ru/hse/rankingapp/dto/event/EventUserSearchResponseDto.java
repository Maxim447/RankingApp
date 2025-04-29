package ru.hse.rankingapp.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Информация о пользователях принимающих участие в заплыве.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Информация о пользователях принимающих участие в заплыве")
public class EventUserSearchResponseDto {

    @Schema(description = "Ссылка на фото пользователя")
    private String image;

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
