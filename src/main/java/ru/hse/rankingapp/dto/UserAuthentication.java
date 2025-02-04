package ru.hse.rankingapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hse.rankingapp.entity.enums.Role;

/**
 * Модель с пользовательскими данными из jwt токена.
 */
@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Schema(description = "Модель с пользовательскими данными из jwt токена")
public class UserAuthentication {

    @Schema(description = "Почта пользователя")
    private String email;

    @Schema(description = "true -> организация, false -> спортсмен")
    private boolean isOrganization;

    /**
     * Роль пользователя.
     */
    @Hidden
    @JsonIgnore
    private Role role;
}
