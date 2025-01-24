package ru.hse.rankingapp.dto;

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
public class UserAuthentication {

    private String email;

    private Role role;

    private boolean isOrganization;
}
