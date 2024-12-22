package ru.hse.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.hse.authservice.entity.User;
import ru.hse.authservice.entity.enums.ActionIndex;
import ru.hse.authservice.repository.UserRepository;
import ru.hse.commonmodule.enums.BusinessExceptionsEnum;
import ru.hse.commonmodule.exception.BusinessException;

/**
 * Реализация UserDetailsService.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Получить пользователя по электронной почте.
     *
     * @param email - электронная почта
     * @return пользователь
     */
    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(BusinessExceptionsEnum.USER_NOT_FOUND_BY_EMAIL));

        if (ActionIndex.D.equals(user.getActionIndex())) {
            throw new BusinessException(BusinessExceptionsEnum.USER_DELETED);
        }

        return user;
    }
}
