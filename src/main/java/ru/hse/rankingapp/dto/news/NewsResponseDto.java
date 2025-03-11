package ru.hse.rankingapp.dto.news;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * Дто новости.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Дто новостей")
public class NewsResponseDto {

    @Schema(description = "Id записи")
    private Long id;

    @Schema(description = "Заголовок")
    private String topic;

    @Schema(description = "Основной текст")
    private String text;

    @Schema(description = "Дата с которой будет отображаться новость")
    private LocalDate startDate;

    @Schema(description = "Дата с которой перестанет отображаться новость")
    private LocalDate endDate;

    @Schema(description = "Ссылка на фото 1")
    private String image1;

    @Schema(description = "Ссылка на фото 2")
    private String image2;

    @Schema(description = "Ссылка на фото 3")
    private String image3;
}
