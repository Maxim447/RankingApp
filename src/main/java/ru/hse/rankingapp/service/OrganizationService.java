package ru.hse.rankingapp.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.rankingapp.dto.UserAuthentication;
import ru.hse.rankingapp.dto.organization.OrganizationFullInfoDto;
import ru.hse.rankingapp.dto.organization.OrganizationInfoDto;
import ru.hse.rankingapp.dto.organization.OrganizationSearchParamsDto;
import ru.hse.rankingapp.dto.organization.UpdateIsOpenStatusDto;
import ru.hse.rankingapp.dto.paging.PageRequestDto;
import ru.hse.rankingapp.dto.paging.PageResponseDto;
import ru.hse.rankingapp.dto.user.EmailRequestDto;
import ru.hse.rankingapp.entity.OrganizationEntity;
import ru.hse.rankingapp.entity.TokenEntity;
import ru.hse.rankingapp.entity.UserEntity;
import ru.hse.rankingapp.entity.enums.TokenAction;
import ru.hse.rankingapp.enums.BusinessExceptionsEnum;
import ru.hse.rankingapp.exception.BusinessException;
import ru.hse.rankingapp.mapper.OrganizationMapper;
import ru.hse.rankingapp.repository.AccountRepository;
import ru.hse.rankingapp.repository.OrganizationRepository;
import ru.hse.rankingapp.repository.TokenRepository;
import ru.hse.rankingapp.service.auth.EmailService;
import ru.hse.rankingapp.service.search.OrganizationSearchWithSpec;
import ru.hse.rankingapp.utils.JwtUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Сервис для работы с организацией.
 */
@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final UserService userService;
    private final OrganizationMapper organizationMapper;
    private final EmailService emailService;
    private final OrganizationSearchWithSpec organizationSearchWithSpec;
    private final TokenRepository tokenRepository;
    private final JwtUtils jwtUtils;
    private final AccountRepository accountRepository;

    /**
     * Получить данные об авторизированном пользователе.
     *
     * @return dto c данными об авторизованном пользователе
     */
    public OrganizationInfoDto getAuthenticatedOrganization() {
        UserAuthentication userInfoFromRequest = jwtUtils.getUserInfoFromRequest();

        if (userInfoFromRequest == null || !userInfoFromRequest.isOrganization()) {
            throw new BusinessException(BusinessExceptionsEnum.NOT_ENOUGH_RULES);
        }

        OrganizationEntity organization = organizationRepository.findByEmail(userInfoFromRequest.getEmail());
        return organizationMapper.mapToOrganizationInfoDto(organization);
    }

    /**
     * Изменить электронную почту.
     *
     * @param updateEmailRequestDto dto для изменения электронной почты
     */
    @Transactional
    public void updateEmail(EmailRequestDto updateEmailRequestDto) {
        UserAuthentication userInfoFromRequest = jwtUtils.getUserInfoFromRequest();

        if (userInfoFromRequest == null || !userInfoFromRequest.isOrganization()) {
            throw new BusinessException(BusinessExceptionsEnum.NOT_ENOUGH_RULES);
        }

        String email = updateEmailRequestDto.getEmail();
        boolean exist = accountRepository.existsByEmail(email);

        if (exist) {
            throw new BusinessException(BusinessExceptionsEnum.EMAIL_ALREADY_EXISTS);
        }

        organizationRepository.updateEmailByOldEmail(userInfoFromRequest.getEmail(), email);
        accountRepository.updateEmailByOldEmail(userInfoFromRequest.getEmail(), email);
    }

    /**
     * Изменить признак открытости у организации
     *
     * @param updateIsOpenStatusDto Дто для обновления статуса организации
     */
    @Transactional
    public void updateOpenStatus(UpdateIsOpenStatusDto updateIsOpenStatusDto) {
        UserAuthentication userInfoFromRequest = jwtUtils.getUserInfoFromRequest();

        if (userInfoFromRequest == null || !userInfoFromRequest.isOrganization()) {
            throw new BusinessException(BusinessExceptionsEnum.NOT_ENOUGH_RULES);
        }

        organizationRepository.updateOpenStatusByEmail(userInfoFromRequest.getEmail(), updateIsOpenStatusDto.getIsOpen());
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
     * @param usersEmails почты пользователей
     */
    @Transactional
    public void addUsersToOrganization(Set<String> usersEmails) {
        UserAuthentication userInfoFromRequest = jwtUtils.getUserInfoFromRequest();

        if (userInfoFromRequest == null || !userInfoFromRequest.isOrganization()) {
            throw new BusinessException(BusinessExceptionsEnum.NOT_ENOUGH_RULES);
        }

        OrganizationEntity organization = organizationRepository.findByEmail(userInfoFromRequest.getEmail());

        Set<UserEntity> users = userService.findUsersByEmails(usersEmails);

        List<TokenEntity> tokenEntities = new ArrayList<>();
        CollectionUtils.emptyIfNull(users)
                .forEach(userEntity -> {
                    try {
                        TokenEntity tokenEntity = new TokenEntity(UUID.randomUUID(),
                                TokenAction.ADD_USER_TO_ORGANIZATION,
                                userEntity, organization);

                        emailService.sendConfirmationMessage(tokenEntity);
                        tokenEntities.add(tokenEntity);
                    } catch (MessagingException e) {
                        throw new BusinessException(BusinessExceptionsEnum.CANNOT_SEND_MESSAGE);
                    }
                });

        tokenRepository.saveAll(tokenEntities);
    }

    /**
     * Получить полные данные об авторизированной организации.
     *
     * @return dto c данными об авторизованном пользователе
     */
    public OrganizationFullInfoDto getOrganizationFullInfo() {
        UserAuthentication userInfoFromRequest = jwtUtils.getUserInfoFromRequest();

        if (userInfoFromRequest == null || !userInfoFromRequest.isOrganization()) {
            throw new BusinessException(BusinessExceptionsEnum.NOT_ENOUGH_RULES);
        }

        OrganizationEntity attachedOrganization = organizationRepository.findByEmailWithFetch(userInfoFromRequest.getEmail());

        return organizationMapper.mapToOrganizationFullInfoDto(attachedOrganization);
    }
}
