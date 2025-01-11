package ru.hse.rankingapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.rankingapp.dto.organization.OrganizationInfoDto;
import ru.hse.rankingapp.dto.user.UpdateEmailRequestDto;
import ru.hse.rankingapp.dto.user.UpdatePasswordRequestDto;
import ru.hse.rankingapp.entity.OrganizationEntity;
import ru.hse.rankingapp.mapper.OrganizationMapper;
import ru.hse.rankingapp.repository.OrganizationRepository;
import ru.hse.rankingapp.enums.BusinessExceptionsEnum;
import ru.hse.rankingapp.exception.BusinessException;

/**
 * Сервис для работы с организацией.
 */
@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Получить данные об авторизированном пользователе.
     *
     * @param organization авторизированная организация
     * @return dto c данными об авторизованном пользователе
     */
    public OrganizationInfoDto getAuthenticatedOrganization(OrganizationEntity organization) {
        return organizationMapper.mapToOrganizationInfoDto(organization);
    }

    /**
     * Изменить пароль.
     *
     * @param updatePasswordRequestDto для изменения пароля
     * @param organization             авторизированная организация
     */
    @Transactional
    public void updatePassword(UpdatePasswordRequestDto updatePasswordRequestDto, OrganizationEntity organization) {
        if (updatePasswordRequestDto.getNewPassword().equals(updatePasswordRequestDto.getOldPassword()) ||
                passwordEncoder.matches(updatePasswordRequestDto.getNewPassword(), organization.getPassword())) {
            throw new BusinessException(BusinessExceptionsEnum.NEW_PASSWORD_EQUALS_OLD_PASSWORD);
        }

        if (!passwordEncoder.matches(updatePasswordRequestDto.getOldPassword(), organization.getPassword())) {
            throw new BusinessException(BusinessExceptionsEnum.WRONG_OLD_PASSWORD);
        }

        organizationRepository.updatePasswordById(organization.getId(), passwordEncoder.encode(updatePasswordRequestDto.getNewPassword()));
    }

    /**
     * Изменить электронную почту.
     *
     * @param updateEmailRequestDto dto для изменения электронной почты
     * @param organization          авторизированная организация
     */
    @Transactional
    public void updateEmail(UpdateEmailRequestDto updateEmailRequestDto, OrganizationEntity organization) {
        String email = updateEmailRequestDto.getEmail();
        boolean exist = organizationRepository.existsByEmail(email);
        if (exist) {
            throw new BusinessException(BusinessExceptionsEnum.EMAIL_ALREADY_EXISTS);
        }

        organizationRepository.updateEmailById(organization.getId(), email);
    }
}
