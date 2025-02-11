package ru.hse.rankingapp.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.RedirectView;
import ru.hse.rankingapp.dto.paging.PageRequestDto;
import ru.hse.rankingapp.dto.paging.PageResponseDto;
import ru.hse.rankingapp.dto.user.EmailRequestDto;
import ru.hse.rankingapp.dto.user.UpdatePasswordRequestDto;
import ru.hse.rankingapp.dto.user.UpdatePhoneRequestDto;
import ru.hse.rankingapp.dto.user.UserInfoDto;
import ru.hse.rankingapp.dto.user.UserSearchParamsDto;
import ru.hse.rankingapp.entity.OrganizationEntity;
import ru.hse.rankingapp.entity.TokenEntity;
import ru.hse.rankingapp.entity.UserEntity;
import ru.hse.rankingapp.entity.enums.ActionIndex;
import ru.hse.rankingapp.entity.enums.TokenAction;
import ru.hse.rankingapp.enums.BusinessExceptionsEnum;
import ru.hse.rankingapp.exception.BusinessException;
import ru.hse.rankingapp.mapper.UserMapper;
import ru.hse.rankingapp.repository.TokenRepository;
import ru.hse.rankingapp.repository.UserRepository;
import ru.hse.rankingapp.service.search.UserSearchWithSpec;

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
    private final PasswordEncoder passwordEncoder;
    private final UserSearchWithSpec userSearchWithSpec;
    private final EventService eventService;
    private final TokenRepository tokenRepository;

    @Value("${redirect.front-main}")
    private String frontMainPage;

    /**
     * Получить данные об авторизированном пользователе.
     *
     * @param user авторизированный пользователь
     * @return dto c данными об авторизованном пользователе
     */
    public UserInfoDto getAuthenticatedUser(UserEntity user) {
        return userMapper.mapToUserInfoDto(user);
    }

    /**
     * Изменить номер телефона.
     *
     * @param updatePhoneRequestDto dto для изменения номера телефона
     * @param user                  авторизированный пользователь
     */
    @Transactional
    public void updatePhone(UpdatePhoneRequestDto updatePhoneRequestDto, UserEntity user) {
        String phone = updatePhoneRequestDto.getPhone();
        boolean exist = userRepository.existsByPhone(phone);
        if (exist) {
            throw new BusinessException(BusinessExceptionsEnum.PHONE_ALREADY_EXISTS);
        }

        userRepository.updatePhoneById(user.getId(), phone);
    }

    /**
     * Изменить пароль.
     *
     * @param updatePasswordRequestDto для изменения пароля
     * @param user                     авторизированный пользователь
     */
    @Transactional
    public void updatePassword(UpdatePasswordRequestDto updatePasswordRequestDto, UserEntity user) {
        if (updatePasswordRequestDto.getNewPassword().equals(updatePasswordRequestDto.getOldPassword()) ||
                passwordEncoder.matches(updatePasswordRequestDto.getNewPassword(), user.getPassword())) {
            throw new BusinessException(BusinessExceptionsEnum.NEW_PASSWORD_EQUALS_OLD_PASSWORD);
        }

        if (!passwordEncoder.matches(updatePasswordRequestDto.getOldPassword(), user.getPassword())) {
            throw new BusinessException(BusinessExceptionsEnum.WRONG_OLD_PASSWORD);
        }

        userRepository.updatePasswordById(user.getId(), passwordEncoder.encode(updatePasswordRequestDto.getNewPassword()));
    }

    /**
     * Изменить электронную почту.
     *
     * @param updateEmailRequestDto dto для изменения электронной почты
     * @param user                  авторизированный пользователь
     */
    @Transactional
    public void updateEmail(EmailRequestDto updateEmailRequestDto, UserEntity user) {
        String email = updateEmailRequestDto.getEmail();
        boolean exist = userRepository.existsByEmail(email);
        if (exist) {
            throw new BusinessException(BusinessExceptionsEnum.EMAIL_ALREADY_EXISTS);
        }

        userRepository.updateEmailById(user.getId(), email);
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
     * @param entity    Сущность пользователя
     * @param eventUuid Юид заплыва
     */
    public void addToEvent(UserEntity entity, UUID eventUuid) {
        if (entity == null) {
            throw new BusinessException(BusinessExceptionsEnum.NOT_ENOUGH_RULES);
        }

        UserEntity user = userRepository.findByEmail(entity.getEmail())
                .orElseThrow(() -> new BusinessException(BusinessExceptionsEnum.USER_NOT_FOUND_BY_EMAIL));

        eventService.addUserToEvent(user, eventUuid);
    }
}
