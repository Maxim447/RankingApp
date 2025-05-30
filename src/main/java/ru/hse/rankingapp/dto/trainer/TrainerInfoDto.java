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

    @Schema(description = "Образование тренера")
    private String education;

    @Schema(description = "Специализация тренера")
    private String specialization;

    @Schema(description = "Достижения тренера")
    private String achievements;

    @Schema(description = "Фото")
    private String image;
}
