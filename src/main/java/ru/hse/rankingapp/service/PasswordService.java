package ru.hse.rankingapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.rankingapp.dto.password.PasswordChangeDto;
import ru.hse.rankingapp.dto.password.PasswordValidateTokenDto;
import ru.hse.rankingapp.dto.user.EmailRequestDto;
import ru.hse.rankingapp.dto.user.UpdatePasswordRequestDto;
import ru.hse.rankingapp.entity.AccountEntity;
import ru.hse.rankingapp.entity.OrganizationEntity;
import ru.hse.rankingapp.entity.TokenEntity;
import ru.hse.rankingapp.entity.UserEntity;
import ru.hse.rankingapp.entity.enums.ActionIndex;
import ru.hse.rankingapp.entity.enums.TokenAction;
import ru.hse.rankingapp.enums.BusinessExceptionsEnum;
import ru.hse.rankingapp.exception.BusinessException;
import ru.hse.rankingapp.repository.AccountRepository;
import ru.hse.rankingapp.repository.OrganizationRepository;
import ru.hse.rankingapp.repository.TokenRepository;
import ru.hse.rankingapp.repository.UserRepository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для работы с паролем.
 */
@Service
@RequiredArgsConstructor
public class PasswordService {

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    /**
     * Изменить пароль.
     *
     * @param passwordChangeDto дто для изменения пароля
     */
    @Transactional
    public void changePassword(PasswordChangeDto passwordChangeDto) {
        if (!passwordChangeDto.getPassword().equals(passwordChangeDto.getConfirmPassword())) {
            throw new BusinessException(BusinessExceptionsEnum.PASSWORD_NOT_EQUALS_CONFIRM_PASSWORD);
        }

        TokenEntity tokenEntity = tokenRepository.findByUuid(passwordChangeDto.getToken())
                .orElseThrow(() -> new BusinessException("Токен на восстановление пароля не обнаружен", HttpStatus.NOT_FOUND));

        TokenAction action = tokenEntity.getTokenAction();

        String email;
        if (TokenAction.RESET_PASSWORD_USER.equals(action)) {
            UserEntity user = tokenEntity.getUser();
            email = user.getEmail();
        } else {
            OrganizationEntity organization = tokenEntity.getOrganization();
            email = organization.getEmail();
        }

        accountRepository.updatePasswordByEmail(email, passwordEncoder.encode(passwordChangeDto.getPassword()));

        tokenEntity.setActionIndex(ActionIndex.D);
        tokenEntity.setModifyDttm(OffsetDateTime.now());
        tokenRepository.save(tokenEntity);
    }

    /**
     * Отправить письмо с восстановлением пароля.
     *
     * @param emailRequestDto почта для отправления
     */
    public void recoveryPassword(EmailRequestDto emailRequestDto) {
        String email = emailRequestDto.getEmail();
        Optional<UserEntity> userOpt = userRepository.findByEmailOpt(email);

        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            UUID uuid = emailService.sendUserRecoveryPasswordEmail(user);

            TokenEntity token = new TokenEntity(uuid, TokenAction.RESET_PASSWORD_USER);
            token.setUser(user);

            tokenRepository.save(token);
            return;
        }

        Optional<OrganizationEntity> organizationOpt = organizationRepository.findByEmailOpt(email);

        if (organizationOpt.isPresent()) {
            OrganizationEntity organization = organizationOpt.get();
            UUID uuid = emailService.sendOrganizationRecoveryPasswordEmail(organization);

            TokenEntity token = new TokenEntity(uuid, TokenAction.RESET_PASSWORD_ORGANIZATION);
            token.setOrganization(organization);

            tokenRepository.save(token);
            return;
        }

        throw new BusinessException(BusinessExceptionsEnum.USER_NOT_FOUND_BY_EMAIL);
    }

    /**
     * Провалидировать токен на смену пароля.
     */
    public void validateToken(PasswordValidateTokenDto passwordValidateTokenDto) {
        UUID token = passwordValidateTokenDto.getToken();
        Optional<TokenEntity> tokenEntityOpt = tokenRepository.findById(token);

        if (tokenEntityOpt.isEmpty()) {
            throw new BusinessException("Токен на восстановление пароля не обнаружен", HttpStatus.NOT_FOUND);
        }

        TokenEntity tokenEntity = tokenEntityOpt.get();

        if (ActionIndex.D.equals(tokenEntity.getActionIndex())) {
            throw new BusinessException("Токен на восстановление пароля был использован ранее", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Обновить пароль пользователя.
     *
     * @param updatePasswordRequestDto Дто для обновления пароля
     */
    @Transactional
    public void updateAccountPassword(UpdatePasswordRequestDto updatePasswordRequestDto, AccountEntity accountEntity) {
        if (updatePasswordRequestDto.getNewPassword().equals(updatePasswordRequestDto.getOldPassword()) ||
                passwordEncoder.matches(updatePasswordRequestDto.getNewPassword(), accountEntity.getPassword())) {
            throw new BusinessException(BusinessExceptionsEnum.NEW_PASSWORD_EQUALS_OLD_PASSWORD);
        }

        if (!passwordEncoder.matches(updatePasswordRequestDto.getOldPassword(), accountEntity.getPassword())) {
            throw new BusinessException(BusinessExceptionsEnum.WRONG_OLD_PASSWORD);
        }

        accountRepository.updatePasswordByEmail(accountEntity.getEmail(), passwordEncoder.encode(updatePasswordRequestDto.getNewPassword()));
    }
}
