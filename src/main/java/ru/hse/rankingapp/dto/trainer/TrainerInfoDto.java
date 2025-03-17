package ru.hse.rankingapp.dto.trainer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Информация о тренере.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Информация о тренере")
public class TrainerInfoDto {

    @Schema(description = "Id")
    private Long id;

    @Schema(description = "Имя")
    private String firstName;

    @Schema(description = "Фамилия")
    private String lastName;

    @Schema(description = "Отчество")
    private String middleName;

    @Schema(description = "Описание тренера")
    private String description;

    @Schema(description = "Фото")
    private String image;
}
