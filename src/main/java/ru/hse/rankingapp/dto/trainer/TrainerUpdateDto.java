package ru.hse.rankingapp.dto.trainer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

@Data
@Accessors(chain = true)
@Schema(description = "Дто для обновления тренера")
public class TrainerUpdateDto {

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

    @Schema(description = "Фотография")
    private MultipartFile image;

    @Schema(description = "Нужно ли обновить фото")
    private Boolean isNeedUpdatePhoto;
}
