package ru.hse.rankingapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.rankingapp.dto.user.UpdateEmailRequestDto;
import ru.hse.rankingapp.dto.user.UpdatePasswordRequestDto;
import ru.hse.rankingapp.dto.user.UpdatePhoneRequestDto;
import ru.hse.rankingapp.dto.user.UserInfoDto;
import ru.hse.rankingapp.entity.User;
import ru.hse.rankingapp.mapper.UserMapper;
import ru.hse.rankingapp.repository.UserRepository;
import ru.hse.rankingapp.enums.BusinessExceptionsEnum;
import ru.hse.rankingapp.exception.BusinessException;

/**
 * Сервис для работы с пользователем.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Получить данные об авторизированном пользователе.
     *
     * @param user авторизированный пользователь
     * @return dto c данными об авторизованном пользователе
     */
    public UserInfoDto getAuthenticatedUser(User user) {
        return userMapper.mapToUserInfoDto(user);
    }

    /**
     * Изменить номер телефона.
     *
     * @param updatePhoneRequestDto dto для изменения номера телефона
     * @param user                  авторизированный пользователь
     */
    @Transactional
    public void updatePhone(UpdatePhoneRequestDto updatePhoneRequestDto, User user) {
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
    public void updatePassword(UpdatePasswordRequestDto updatePasswordRequestDto, User user) {
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
    public void updateEmail(UpdateEmailRequestDto updateEmailRequestDto, User user) {
        String email = updateEmailRequestDto.getEmail();
        boolean exist = userRepository.existsByEmail(email);
        if (exist) {
            throw new BusinessException(BusinessExceptionsEnum.EMAIL_ALREADY_EXISTS);
        }

        userRepository.updateEmailById(user.getId(), email);
    }
}
