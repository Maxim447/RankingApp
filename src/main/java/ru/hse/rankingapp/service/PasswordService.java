package ru.hse.rankingapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.rankingapp.dto.password.PasswordChangeDto;
import ru.hse.rankingapp.dto.password.PasswordValidateTokenDto;
import ru.hse.rankingapp.dto.user.EmailRequestDto;
import ru.hse.rankingapp.entity.OrganizationEntity;
import ru.hse.rankingapp.entity.TokenEntity;
import ru.hse.rankingapp.entity.UserEntity;
import ru.hse.rankingapp.entity.enums.ActionIndex;
import ru.hse.rankingapp.entity.enums.TokenAction;
import ru.hse.rankingapp.enums.BusinessExceptionsEnum;
import ru.hse.rankingapp.exception.BusinessException;
import ru.hse.rankingapp.repository.OrganizationRepository;
import ru.hse.rankingapp.repository.TokenRepository;
import ru.hse.rankingapp.repository.UserRepository;
import ru.hse.rankingapp.service.auth.EmailService;

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

        if (TokenAction.RESET_PASSWORD_USER.equals(action)) {
            UserEntity user = tokenEntity.getUser();

            userRepository.updatePasswordById(user.getId(), passwordEncoder.encode(passwordChangeDto.getPassword()));
        } else {
            OrganizationEntity organization = tokenEntity.getOrganization();

            organizationRepository.updatePasswordById(organization.getId(), passwordEncoder.encode(passwordChangeDto.getPassword()));
        }

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
        Optional<UserEntity> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            UUID uuid = emailService.sendUserRecoveryPasswordEmail(user);

            TokenEntity token = new TokenEntity(uuid, TokenAction.RESET_PASSWORD_USER);
            token.setUser(user);

            tokenRepository.save(token);
            return;
        }

        Optional<OrganizationEntity> organizationOpt = organizationRepository.findByEmail(email);

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
}
