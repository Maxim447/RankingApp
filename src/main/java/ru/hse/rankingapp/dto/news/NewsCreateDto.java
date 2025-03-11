package ru.hse.rankingapp.dto.news;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

/**
 * Дто для создания новости.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Дто для создания новости")
public class NewsCreateDto {

    @Schema(description = "Заголовок")
    @NotNull(message = "Заголовок должен быть заполнен")
    private String topic;

    @Schema(description = "Основной текст")
    @NotNull(message = "Основной текст должен быть заполнен")
    private String text;

    @Schema(description = "Дата с которой будет отображаться новость")
    @NotNull(message = "Дата с которой будет отображаться новость должна быть заполнена")
    private LocalDate startDate;

    @Schema(description = "Дата с которой перестанет отображаться новость")
    @NotNull(message = "Дата с которой перестанет отображаться новость должна быть заполнена")
    private LocalDate endDate;

    @Schema(description = "Фото 1")
    private MultipartFile image1;

    @Schema(description = "Фото 2")
    private MultipartFile image2;

    @Schema(description = "Фото 3")
    private MultipartFile image3;
}
