package ru.hse.rankingapp.dto.password;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

/**
 * Дто с токеном.
 */
@Data
@Accessors(chain = true)
public class PasswordValidateTokenDto {

    private UUID token;
}
