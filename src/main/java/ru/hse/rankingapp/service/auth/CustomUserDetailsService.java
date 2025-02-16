package ru.hse.rankingapp.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.hse.rankingapp.entity.AccountEntity;
import ru.hse.rankingapp.enums.BusinessExceptionsEnum;
import ru.hse.rankingapp.exception.BusinessException;
import ru.hse.rankingapp.repository.AccountRepository;

import java.util.Optional;

/**
 * Реализация UserDetailsService.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    /**
     * Получить пользователя по электронной почте.
     *
     * @param email - электронная почта
     * @return пользователь
     */
    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<AccountEntity> accountEntityOptional = accountRepository.findByEmail(email);

        if (accountEntityOptional.isPresent()) {
            return accountEntityOptional.get();
        }

        throw new BusinessException(BusinessExceptionsEnum.USER_NOT_FOUND_BY_EMAIL);
    }
}
