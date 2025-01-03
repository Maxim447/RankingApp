package ru.hse.rankingapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Ответ на получение кода подтверждения электронной почты.
 */
@Data
@Schema(description = "Ответ на получение кода подтверждения электронной почты")
public class VerificationCodeResponseDto {

    @Schema(description = "Код подтверждения")
    private String verificationCode;
}
