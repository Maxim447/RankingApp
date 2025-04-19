package ru.hse.rankingapp.dto.professional.records;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.hse.rankingapp.deserializer.LocalTimeDeserializer;
import ru.hse.rankingapp.entity.enums.Gender;

import java.time.LocalTime;

/**
 * Дто для создания новой записи в таблице рекордов.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Дто для создания новой записи в таблице рекордов")
public class CreateProfessionalRecordDto {

    @Schema(description = "Дистанция")
    @NotNull(message = "Время обязательна к заполнению")
    private Integer distance;

    @Schema(description = "Стиль")
    @NotNull(message = "Стиль обязателен к заполнению")
    private String style;

    @Schema(description = "Гендер")
    @NotNull(message = "Пол обязателен к заполнению")
    private Gender gender;

    @Schema(description = "Время")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @NotNull(message = "Время обязательно к заполнению")
    private LocalTime time;
}
