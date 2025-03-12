package ru.hse.rankingapp.dto.news;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

/**
 * Дто для обновления новости.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Дто для обновления новости. Будут изменены поля, которые не null")
public class NewsUpdateDto {

    @Schema(description = "Заголовок")
    private String topic;

    @Schema(description = "Основной текст")
    private String text;

    @Schema(description = "Дата с которой будет отображаться новость")
    private LocalDate startDate;

    @Schema(description = "Дата с которой перестанет отображаться новость")
    private LocalDate endDate;

    @Schema(description = "Фото 1")
    private MultipartFile image1;

    @Schema(description = "Нужно ли обновить первое фото. True -> фото 1 измениться на то что в переменной image1, если image1 null то удалится. False -> фото останется таким каким было")
    private Boolean isNeedUpdateImage1;

    @Schema(description = "Фото 2")
    private MultipartFile image2;

    @Schema(description = "Нужно ли обновить второе фото. True -> фото 2 измениться на то что в переменной image2, если image2 null то удалится. False -> фото останется таким каким было")
    private Boolean isNeedUpdateImage2;

    @Schema(description = "Фото 3")
    private MultipartFile image3;

    @Schema(description = "Нужно ли обновить третье фото. True -> фото 3 измениться на то что в переменной image3, если image3 null то удалится. False -> фото останется таким каким было")
    private Boolean isNeedUpdateImage3;
}
