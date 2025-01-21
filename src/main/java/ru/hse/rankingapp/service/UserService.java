package ru.hse.rankingapp.service;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.rankingapp.dto.paging.PageRequestDto;
import ru.hse.rankingapp.dto.paging.PageResponseDto;
import ru.hse.rankingapp.dto.user.ConfirmInviteDto;
import ru.hse.rankingapp.dto.user.UpdateEmailRequestDto;
import ru.hse.rankingapp.dto.user.UpdatePasswordRequestDto;
import ru.hse.rankingapp.dto.user.UpdatePhoneRequestDto;
import ru.hse.rankingapp.dto.user.UserInfoDto;
import ru.hse.rankingapp.dto.user.UserSearchParamsDto;
import ru.hse.rankingapp.entity.OrganizationEntity;
import ru.hse.rankingapp.entity.UserEntity;
import ru.hse.rankingapp.enums.BusinessExceptionsEnum;
import ru.hse.rankingapp.exception.BusinessException;
import ru.hse.rankingapp.mapper.UserMapper;
import ru.hse.rankingapp.repository.OrganizationRepository;
import ru.hse.rankingapp.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Сервис для работы с пользователем.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final OrganizationRepository organizationRepository;

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
    public void updateEmail(UpdateEmailRequestDto updateEmailRequestDto, UserEntity user) {
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
        Specification<UserEntity> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchParams.getFirstName() != null) {
                predicates.add(criteriaBuilder.equal(root.get("firstName"), searchParams.getFirstName()));
            }

            if (searchParams.getLastName() != null) {
                predicates.add(criteriaBuilder.equal(root.get("lastName"), searchParams.getLastName()));
            }

            if (searchParams.getMiddleName() != null) {
                predicates.add(criteriaBuilder.equal(root.get("middleName"), searchParams.getMiddleName()));
            }

            if (searchParams.getEmail() != null) {
                predicates.add(criteriaBuilder.equal(root.get("email"), searchParams.getEmail()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

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
     * @param inviteDto информация о пользователе и организации.
     */
    @Transactional
    public void confirmInviteIntoOrganization(ConfirmInviteDto inviteDto) {
        UserEntity user = userRepository.findByEmail(inviteDto.getUserEmail())
                .orElseThrow(() -> new BusinessException(BusinessExceptionsEnum.USER_NOT_FOUND_BY_EMAIL));

        OrganizationEntity organization = organizationRepository.findByEmail(inviteDto.getOrganizationEmail())
                .orElseThrow(() -> new BusinessException(BusinessExceptionsEnum.ORGANIZATION_NOT_FOUND_BY_EMAIL));

        user.addOrganization(organization);

        userRepository.save(user);
    }
}
