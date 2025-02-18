package ru.hse.rankingapp.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.RedirectView;
import ru.hse.rankingapp.dto.UserAuthentication;
import ru.hse.rankingapp.dto.paging.PageRequestDto;
import ru.hse.rankingapp.dto.paging.PageResponseDto;
import ru.hse.rankingapp.dto.user.EmailRequestDto;
import ru.hse.rankingapp.dto.user.UpdatePhoneRequestDto;
import ru.hse.rankingapp.dto.user.UserInfoDto;
import ru.hse.rankingapp.dto.user.UserSearchParamsDto;
import ru.hse.rankingapp.entity.OrganizationEntity;
import ru.hse.rankingapp.entity.TokenEntity;
import ru.hse.rankingapp.entity.UserEntity;
import ru.hse.rankingapp.entity.enums.ActionIndex;
import ru.hse.rankingapp.entity.enums.Role;
import ru.hse.rankingapp.entity.enums.TokenAction;
import ru.hse.rankingapp.enums.BusinessExceptionsEnum;
import ru.hse.rankingapp.exception.BusinessException;
import ru.hse.rankingapp.mapper.UserMapper;
import ru.hse.rankingapp.repository.AccountRepository;
import ru.hse.rankingapp.repository.TokenRepository;
import ru.hse.rankingapp.repository.UserRepository;
import ru.hse.rankingapp.service.search.UserSearchWithSpec;
import ru.hse.rankingapp.utils.JwtUtils;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * Сервис для работы с пользователем.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserSearchWithSpec userSearchWithSpec;
    private final EventService eventService;
    private final TokenRepository tokenRepository;
    private final JwtUtils jwtUtils;
    private final AccountRepository accountRepository;

    @Value("${redirect.front-main}")
    private String frontMainPage;

    /**
     * Получить данные об авторизированном пользователе.
     *
     * @return dto c данными об авторизованном пользователе
     */
    public UserInfoDto getAuthenticatedUser() {
        UserAuthentication userInfoFromRequest = jwtUtils.getUserInfoFromRequest();

        if (userInfoFromRequest == null || !userInfoFromRequest.getRoles().contains(Role.USER)) {
            throw new BusinessException(BusinessExceptionsEnum.NOT_ENOUGH_RULES);
        }

        UserEntity userEntity = userRepository.findByEmail(userInfoFromRequest.getEmail());

        return userMapper.mapToUserInfoDto(userEntity);
    }

    /**
     * Изменить номер телефона.
     *
     * @param updatePhoneRequestDto dto для изменения номера телефона
     */
    @Transactional
    public void updatePhone(UpdatePhoneRequestDto updatePhoneRequestDto) {
        UserAuthentication userInfoFromRequest = jwtUtils.getUserInfoFromRequest();

        if (userInfoFromRequest == null || !userInfoFromRequest.getRoles().contains(Role.USER)) {
            throw new BusinessException(BusinessExceptionsEnum.NOT_ENOUGH_RULES);
        }

        String phone = updatePhoneRequestDto.getPhone();
        boolean exist = userRepository.existsByPhone(phone);
        if (exist) {
            throw new BusinessException(BusinessExceptionsEnum.PHONE_ALREADY_EXISTS);
        }

        userRepository.updatePhoneByEmail(userInfoFromRequest.getEmail(), phone);
    }

    /**
     * Изменить электронную почту.
     *
     * @param updateEmailRequestDto dto для изменения электронной почты
     */
    @Transactional
    public void updateEmail(EmailRequestDto updateEmailRequestDto) {
        UserAuthentication userInfoFromRequest = jwtUtils.getUserInfoFromRequest();

        if (userInfoFromRequest == null || !userInfoFromRequest.getRoles().contains(Role.USER)) {
            throw new BusinessException(BusinessExceptionsEnum.NOT_ENOUGH_RULES);
        }

        String email = updateEmailRequestDto.getEmail();
        boolean exist = accountRepository.existsByEmail(email);
        if (exist) {
            throw new BusinessException(BusinessExceptionsEnum.EMAIL_ALREADY_EXISTS);
        }

        userRepository.updateEmailByOldEmail(userInfoFromRequest.getEmail(), email);
        accountRepository.updateEmailByOldEmail(userInfoFromRequest.getEmail(), email);
    }

    /**
     * Получить пользователей по параметрам поиска.
     *
     * @param searchParams поисковые параметры
     * @param pageRequest  пагинация
     * @return пагинированный ответ
     */
    public PageResponseDto<UserInfoDto> searchUsers(UserSearchParamsDto searchParams, PageRequestDto pageRequest) {
        Specification<UserEntity> specification = userSearchWithSpec.searchWithSpec(searchParams);

        Page<UserInfoDto> userPage = userRepository.findAll(specification, pageRequest.toPageRequest())
                .map(userMapper::mapToUserInfoDto);

        return new PageResponseDto<>(userPage.getTotalElements(), userPage.getTotalPages(), userPage.getContent());
    }

    /**
     * Найти пользователей по почтам.
     *
     * @param usersEmails почты
     * @return Сущности пользователей
     */
    public Set<UserEntity> findUsersByEmails(Set<String> usersEmails) {
        Set<UserEntity> users = userRepository.findByEmails(usersEmails);

        if (CollectionUtils.isEmpty(users)) {
            throw new BusinessException(
                    String.format("Не удалось найти пользователей по почтам %s", usersEmails), HttpStatus.NOT_FOUND);
        }

        return users;
    }

    /**
     * Принять приглашение на вступление к организации.
     *
     * @param token токен на добавление
     */
    @Transactional
    public RedirectView confirmInviteIntoOrganization(UUID token) {
        TokenEntity tokenEntity = tokenRepository.findByIdFetched(token)
                .orElseThrow(() -> new BusinessException("Токен на восстановление пароля не обнаружен", HttpStatus.NOT_FOUND));

        if (!TokenAction.ADD_USER_TO_ORGANIZATION.equals(tokenEntity.getTokenAction())) {
            throw new BusinessException("Невалидный токен", HttpStatus.BAD_REQUEST);
        }

        UserEntity user = tokenEntity.getUser();
        if (user == null) {
            throw new BusinessException("В токене отсутствует пользователь", HttpStatus.NOT_FOUND);
        }

        OrganizationEntity organization = tokenEntity.getOrganization();

        if (organization == null) {
            throw new BusinessException("В токене отсутствует организация", HttpStatus.NOT_FOUND);
        }

        user.addOrganization(organization);

        userRepository.save(user);

        tokenEntity.setModifyDttm(OffsetDateTime.now());
        tokenEntity.setActionIndex(ActionIndex.D);

        tokenRepository.save(tokenEntity);

        return new RedirectView(frontMainPage);
    }

    /**
     * Добавить пользователя к заплыву.
     *
     * @param eventUuid Юид заплыва
     */
    public void addToEvent(UUID eventUuid) {
        UserAuthentication userInfoFromRequest = jwtUtils.getUserInfoFromRequest();

        if (userInfoFromRequest == null || !userInfoFromRequest.getRoles().contains(Role.USER)) {
            throw new BusinessException(BusinessExceptionsEnum.NOT_ENOUGH_RULES);
        }

        UserEntity user = userRepository.findByEmailOpt(userInfoFromRequest.getEmail())
                .orElseThrow(() -> new BusinessException(BusinessExceptionsEnum.USER_NOT_FOUND_BY_EMAIL));

        eventService.addUserToEvent(user, eventUuid);
    }
}
