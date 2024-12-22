package ru.hse.commonmodule.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hse.commonmodule.enums.Role;

/**
 * Модель с пользовательскими данными из jwt токена.
 */
@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class UserAuthentication {

    private Long id;

    private String email;

    private String phone;

    private String fio;

    private Role role;
}
