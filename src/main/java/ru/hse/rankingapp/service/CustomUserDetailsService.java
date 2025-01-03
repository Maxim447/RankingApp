package ru.hse.rankingapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.hse.rankingapp.entity.Organization;
import ru.hse.rankingapp.entity.User;
import ru.hse.rankingapp.entity.enums.ActionIndex;
import ru.hse.rankingapp.repository.OrganizationRepository;
import ru.hse.rankingapp.repository.UserRepository;
import ru.hse.rankingapp.enums.BusinessExceptionsEnum;
import ru.hse.rankingapp.exception.BusinessException;

import java.util.Optional;

/**
 * Реализация UserDetailsService.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;

    /**
     * Получить пользователя по электронной почте.
     *
     * @param email - электронная почта
     * @return пользователь
     */
    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (ActionIndex.D.equals(user.getActionIndex())) {
                throw new BusinessException(BusinessExceptionsEnum.USER_DELETED);
            }

            return user;
        }

        Optional<Organization> organizationOptional = organizationRepository.findByEmail(email);

        if (organizationOptional.isPresent()) {
            Organization organization = organizationOptional.get();

            if (ActionIndex.D.equals(organization.getActionIndex())) {
                throw new BusinessException(BusinessExceptionsEnum.ORGANIZATION_DELETED);
            }

            return organization;
        }

        throw new BusinessException(BusinessExceptionsEnum.USER_NOT_FOUND_BY_EMAIL);
    }
}
