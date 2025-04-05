package ru.hse.rankingapp.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import ru.hse.rankingapp.dto.UserAuthentication;
import ru.hse.rankingapp.dto.curator.CuratorUserCreateDto;
import ru.hse.rankingapp.dto.curator.EmailPasswordDto;
import ru.hse.rankingapp.dto.login.LoginResponseDto;
import ru.hse.rankingapp.dto.organization.OrganizationFullInfoDto;
import ru.hse.rankingapp.dto.organization.OrganizationInfoDto;
import ru.hse.rankingapp.dto.organization.OrganizationSearchParamsDto;
import ru.hse.rankingapp.dto.organization.UpdateIsOpenStatusDto;
import ru.hse.rankingapp.dto.paging.PageRequestDto;
import ru.hse.rankingapp.dto.paging.PageResponseDto;
import ru.hse.rankingapp.dto.user.EmailRequestDto;
import ru.hse.rankingapp.entity.AccountEntity;
import ru.hse.rankingapp.entity.NotificationEntity;
import ru.hse.rankingapp.entity.OrganizationEntity;
import ru.hse.rankingapp.entity.TokenEntity;
import ru.hse.rankingapp.entity.UserEntity;
import ru.hse.rankingapp.entity.enums.ActionIndex;
import ru.hse.rankingapp.entity.enums.Role;
import ru.hse.rankingapp.entity.enums.TokenAction;
import ru.hse.rankingapp.enums.BusinessExceptionsEnum;
import ru.hse.rankingapp.enums.FileExtensionsEnum;
import ru.hse.rankingapp.exception.BusinessException;
import ru.hse.rankingapp.mapper.OrganizationMapper;
import ru.hse.rankingapp.repository.AccountRepository;
import ru.hse.rankingapp.repository.NotificationRepository;
import ru.hse.rankingapp.repository.OrganizationRepository;
import ru.hse.rankingapp.repository.TokenRepository;
import ru.hse.rankingapp.service.auth.JwtService;
import ru.hse.rankingapp.service.search.OrganizationSearchWithSpec;
import ru.hse.rankingapp.utils.JwtUtils;
import ru.hse.rankingapp.utils.PasswordGenerator;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private final JwtService jwtService;
    private final JwtUtils jwtUtils;
    private final AccountRepository accountRepository;
    private final FileService fileService;
    private final NotificationRepository notificationRepository;

    @Value("${redirect.front-main}")
    private String frontMainPage;

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
    public LoginResponseDto updateEmail(EmailRequestDto updateEmailRequestDto) {
        UserAuthentication userInfoFromRequest = jwtUtils.getUserInfoFromRequest();

        if (userInfoFromRequest == null || !userInfoFromRequest.isOrganization()) {
            throw new BusinessException(BusinessExceptionsEnum.NOT_ENOUGH_RULES);
        }

        String email = updateEmailRequestDto.getEmail();
        boolean exist = accountRepository.existsByEmail(email);

        if (exist) {
            throw new BusinessException(BusinessExceptionsEnum.EMAIL_ALREADY_EXISTS);
        }

        AccountEntity accountEntity = accountRepository.findByEmail(userInfoFromRequest.getEmail());

        organizationRepository.updateEmailByOldEmail(userInfoFromRequest.getEmail(), email);
        accountEntity.setEmail(email);

        return LoginResponseDto.of(jwtService.generateToken(accountEntity));
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

        Map<String, AccountEntity> accountEntities = accountRepository.findByEmails(usersEmails).stream()
                .collect(Collectors.toMap(AccountEntity::getEmail, Function.identity()));

        List<TokenEntity> tokenEntities = new ArrayList<>();
        List<NotificationEntity> notificationEntities = new ArrayList<>();

        CollectionUtils.emptyIfNull(users)
                .forEach(userEntity -> {
                    try {
                        TokenEntity tokenEntity = new TokenEntity(UUID.randomUUID(),
                                TokenAction.ADD_USER_TO_ORGANIZATION,
                                userEntity, organization);

                        emailService.sendConfirmationMessage(tokenEntity);
                        tokenEntities.add(tokenEntity);

                        NotificationEntity notificationEntity = new NotificationEntity();
                        notificationEntity.setAccountEntity(accountEntities.get(userEntity.getEmail()));
                        notificationEntity.setText(
                                "Подтвердите вступление в организацию \"" + organization.getName() + "\" на почте.");

                        notificationEntities.add(notificationEntity);
                    } catch (MessagingException e) {
                        throw new BusinessException(BusinessExceptionsEnum.CANNOT_SEND_MESSAGE);
                    }
                });

        notificationRepository.saveAll(notificationEntities);
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

    /**
     * Загрузить фотографию организации.
     *
     * @param multipartFile файл
     */
    public void uploadImage(MultipartFile multipartFile) {
        UserAuthentication userInfoFromRequest = jwtUtils.getUserInfoFromRequest();

        if (userInfoFromRequest == null || !userInfoFromRequest.getRoles().contains(Role.ORGANIZATION)) {
            throw new BusinessException(BusinessExceptionsEnum.NOT_ENOUGH_RULES);
        }

        Optional.ofNullable(StringUtils.getFilenameExtension(multipartFile.getOriginalFilename()))
                .map(String::toLowerCase)
                .filter(extension -> FileExtensionsEnum.JPEG.getValue().equals(extension) ||
                        FileExtensionsEnum.PNG.getValue().equals(extension))
                .orElseThrow(() -> new BusinessException("Нельзя сохранить файл с таким расширением", HttpStatus.BAD_REQUEST));

        String path = fileService.saveFile(multipartFile);

        organizationRepository.uploadImageByEmail(userInfoFromRequest.getEmail(), path);
    }


    /**
     * Добавить роль куратора.
     *
     * @param email Email организации
     */
    @Transactional
    public void addCurator(String email) {
        boolean existsByEmail = organizationRepository.existsByEmail(email);

        if (!existsByEmail) {
            throw new BusinessException("Организации с почтой = \"" + email + "\" отсутствует.", HttpStatus.NOT_FOUND);
        }

        AccountEntity accountEntity = accountRepository.findByEmail(email);
        accountEntity.addRole(Role.CURATOR);

        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setText("Вам добавлена роль куратора.");
        notificationEntity.setAccountEntity(accountEntity);
        notificationRepository.save(notificationEntity);
    }

    /**
     * Добавить роль куратора.
     *
     * @param token токен
     */
    @Transactional
    public RedirectView addCurator(UUID token) {
        TokenEntity tokenEntity = tokenRepository.findByUuid(token).
                orElseThrow(() -> new BusinessException("Токен не найден.", HttpStatus.NOT_FOUND));

        if (!TokenAction.ADD_ROLE_CURATOR.equals(tokenEntity.getTokenAction())) {
            throw new BusinessException("Невалидный токен.", HttpStatus.BAD_REQUEST);
        }

        OrganizationEntity organization = tokenEntity.getOrganization();

        AccountEntity accountEntity = accountRepository.findByEmail(organization.getEmail());
        accountEntity.addRole(Role.CURATOR);

        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setText("Вам добавлена роль куратора.");
        notificationEntity.setAccountEntity(accountEntity);
        notificationRepository.save(notificationEntity);

        tokenEntity.setActionIndex(ActionIndex.D);
        tokenEntity.setModifyDttm(OffsetDateTime.now());

        return new RedirectView(frontMainPage);
    }

    /**
     * Создать аккаунт пользователя своей организации.
     */
    @Transactional
    public EmailPasswordDto curatorCreateUser(CuratorUserCreateDto curatorUserCreateDto) {
        if (Boolean.TRUE.equals(curatorUserCreateDto.getIsNeedGeneratePassword())) {
            String password = PasswordGenerator.generatePassword(16);
            curatorUserCreateDto.setPassword(password);
        } else {
            if (!curatorUserCreateDto.getPassword().equals(curatorUserCreateDto.getConfirmPassword())) {
                throw new BusinessException(BusinessExceptionsEnum.PASSWORD_NOT_EQUALS_CONFIRM_PASSWORD);
            }
        }

        if (accountRepository.existsByEmail(curatorUserCreateDto.getEmail())) {
            throw new BusinessException(BusinessExceptionsEnum.EMAIL_ALREADY_EXISTS);
        }

        UserAuthentication userInfoFromRequest = jwtUtils.getUserInfoFromRequest();
        String email = userInfoFromRequest.getEmail();

        OrganizationEntity organization = organizationRepository.findByEmail(email);

        UserEntity user = userService.createUser(curatorUserCreateDto);

        organization.addUser(user);

        return new EmailPasswordDto()
                .setEmail(curatorUserCreateDto.getEmail())
                .setPassword(curatorUserCreateDto.getPassword());
    }

    /**
     * Добавить пользователей к организации без приглашения.
     */
    @Transactional
    public void addUsersToOrganizationWithoutConfirmation(Set<String> usersEmails) {
        UserAuthentication userInfoFromRequest = jwtUtils.getUserInfoFromRequest();
        OrganizationEntity organization = organizationRepository.findByEmail(userInfoFromRequest.getEmail());

        userService.findUsersByEmails(usersEmails).forEach(organization::addUser);
    }

    /**
     * Отправить запрос админу на добавление роли куратора.
     */
    public void sendMessageToAdmin(String text) {
        UserAuthentication userInfoFromRequest = jwtUtils.getUserInfoFromRequest();

        if (userInfoFromRequest == null || !userInfoFromRequest.isOrganization()) {
            throw new BusinessException(BusinessExceptionsEnum.NOT_ENOUGH_RULES);
        }

        OrganizationEntity organization = organizationRepository.findByEmail(userInfoFromRequest.getEmail());

        AccountEntity adminAccount = accountRepository.searchAllAdminEmails().stream()
                .findAny()
                .orElseThrow(() -> new BusinessException("Не удалось найти почту админа", HttpStatus.NOT_FOUND));

        UUID token = UUID.randomUUID();
        TokenEntity tokenEntity = new TokenEntity(token, TokenAction.ADD_ROLE_CURATOR, organization);
        try {
            emailService.sendMessageToAddCurator(adminAccount, organization, text, token);
        } catch (MessagingException e) {
            throw new BusinessException(BusinessExceptionsEnum.CANNOT_SEND_MESSAGE);
        }

        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setAccountEntity(adminAccount);
        notificationEntity.setText("Подтвердите добавление роли куратора на почте, для организации \"" + organization.getName() + "\"");

        tokenRepository.save(tokenEntity);
        notificationRepository.save(notificationEntity);
    }
}
