package ru.hse.rankingapp.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Дто ответа для входа.
 */
@Schema(description = "Ответ для входа")
@Data
@AllArgsConstructor(staticName = "of")
public class LoginResponseDto {

    @Schema(description = "Jwt токен")
    private String jwtToken;
}
