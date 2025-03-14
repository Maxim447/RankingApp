package ru.hse.rankingapp.dto.curator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Дто с данными для входа.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Дто с данными для входа")
public class EmailPasswordDto {

    @Schema(description = "Почта")
    private String email;

    @Schema(description = "Пароль")
    private String password;
}
