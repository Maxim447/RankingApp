package ru.hse.rankingapp.dto.trainer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

/**
 * Дто для создания тренера.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Дто для создания тренера")
public class TrainerCreateDto {

    @Schema(description = "Имя")
    @NotNull(message = "Имя должно быть заполнено")
    private String firstName;

    @Schema(description = "Фамилия")
    @NotNull(message = "Фамилия должна быть заполнено")
    private String lastName;

    @Schema(description = "Отчество")
    private String middleName;

    @Schema(description = "Образование тренера")
    private String education;

    @Schema(description = "Специализация тренера")
    private String specialization;

    @Schema(description = "Достижения тренера")
    private String achievements;

    @Schema(description = "Фотография")
    private MultipartFile image;
}
