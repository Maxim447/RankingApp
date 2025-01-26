package ru.hse.rankingapp.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.rankingapp.dto.organization.OrganizationFullInfoDto;
import ru.hse.rankingapp.dto.organization.OrganizationInfoDto;
import ru.hse.rankingapp.dto.organization.OrganizationSearchParamsDto;
import ru.hse.rankingapp.dto.paging.PageRequestDto;
import ru.hse.rankingapp.dto.paging.PageResponseDto;
import ru.hse.rankingapp.dto.user.EmailRequestDto;
import ru.hse.rankingapp.dto.user.UpdatePasswordRequestDto;
import ru.hse.rankingapp.entity.OrganizationEntity;
import ru.hse.rankingapp.entity.UserEntity;
import ru.hse.rankingapp.enums.BusinessExceptionsEnum;
import ru.hse.rankingapp.exception.BusinessException;
import ru.hse.rankingapp.mapper.OrganizationMapper;
import ru.hse.rankingapp.repository.OrganizationRepository;
import ru.hse.rankingapp.service.auth.EmailService;
import ru.hse.rankingapp.service.search.OrganizationSearchWithSpec;

import java.util.Set;

/**
 * Сервис для работы с организацией.
 */
@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final UserService userService;
    private final OrganizationMapper organizationMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final OrganizationSearchWithSpec organizationSearchWithSpec;

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
    public void updateEmail(EmailRequestDto updateEmailRequestDto, OrganizationEntity organization) {
        String email = updateEmailRequestDto.getEmail();
        boolean exist = organizationRepository.existsByEmail(email);
        if (exist) {
            throw new BusinessException(BusinessExceptionsEnum.EMAIL_ALREADY_EXISTS);
        }

        organizationRepository.updateEmailById(organization.getId(), email);
    }

    /**
     * Получить организации по параметрам поиска.
     *
     * @param searchParams поисковые параметры
     * @param pageRequest  пагинация
     * @return пагинированный ответ
     */
    public PageResponseDto<OrganizationInfoDto> searchOrganization(OrganizationSearchParamsDto searchParams, PageRequestDto pageRequest) {
        Specification<OrganizationEntity> specification = organizationSearchWithSpec.searchWithSpec(searchParams);

        Page<OrganizationInfoDto> organizationInfoPage = organizationRepository.findAll(specification, pageRequest.toPageRequest())
                .map(organizationMapper::mapToOrganizationInfoDto);

        return new PageResponseDto<>(organizationInfoPage.getTotalElements(), organizationInfoPage.getTotalPages(), organizationInfoPage.getContent());
    }

    /**
     * Добавить пользователей к организации.
     *
     * @param organization Сущность организации
     * @param usersEmails  почты пользователей
     */
    public void addUsersToOrganization(OrganizationEntity organization, Set<String> usersEmails) {
        if (organization == null) {
            throw new BusinessException(BusinessExceptionsEnum.NOT_ENOUGH_RULES);
        }

        Set<UserEntity> users = userService.findUsersByEmails(usersEmails);

        CollectionUtils.emptyIfNull(users)
                .forEach(userEntity -> {
                    try {
                        emailService.sendConfirmationMessage(userEntity, organization);
                    } catch (MessagingException e) {
                        throw new BusinessException(BusinessExceptionsEnum.CANNOT_SEND_MESSAGE);
                    }
                });
    }

    /**
     * Получить полные данные об авторизированной организации.
     *
     * @param organization авторизированная организация
     * @return dto c данными об авторизованном пользователе
     */
    public OrganizationFullInfoDto getOrganizationFullInfo(OrganizationEntity organization) {
        if (organization == null) {
            throw new BusinessException(BusinessExceptionsEnum.NOT_ENOUGH_RULES);
        }

        OrganizationEntity attachedOrganization = organizationRepository.findOrganizationById(organization.getId());

        return organizationMapper.mapToOrganizationFullInfoDto(attachedOrganization);
    }
}
