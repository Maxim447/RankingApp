package ru.hse.rankingapp.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.rankingapp.dto.UserAuthentication;
import ru.hse.rankingapp.dto.VerificationCodeResponseDto;
import ru.hse.rankingapp.dto.login.LoginRequestDto;
import ru.hse.rankingapp.dto.login.LoginResponseDto;
import ru.hse.rankingapp.dto.organization.SignUpOrganizationRequestDto;
import ru.hse.rankingapp.dto.user.SignUpUserRequestDto;
import ru.hse.rankingapp.entity.AccountEntity;
import ru.hse.rankingapp.entity.OrganizationEntity;
import ru.hse.rankingapp.entity.UserEntity;
import ru.hse.rankingapp.entity.enums.Role;
import ru.hse.rankingapp.enums.BusinessExceptionsEnum;
import ru.hse.rankingapp.exception.BusinessException;
import ru.hse.rankingapp.mapper.OrganizationMapper;
import ru.hse.rankingapp.mapper.UserMapper;
import ru.hse.rankingapp.repository.AccountRepository;
import ru.hse.rankingapp.repository.OrganizationRepository;
import ru.hse.rankingapp.repository.UserRepository;
import ru.hse.rankingapp.utils.JwtUtils;
import ru.hse.rankingapp.utils.Validator;
import ru.hse.rankingapp.utils.VerificationCodeGenerator;

import java.util.Optional;
import java.util.Set;

/**
 * Сервис для работы с аутентификацией пользователя.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final UserMapper userMapper;
    private final OrganizationMapper organizationMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AccountRepository accountRepository;

    /**
     * Получить код для подтверждения электронной почты.
     *
     * @param emailToVerify почта для подтверждения
     * @return dto с кодом подтверждения
     */
    public VerificationCodeResponseDto getEmailVerificationCode(String emailToVerify) {
        Validator.validateLogin(emailToVerify);

        String verificationCode = VerificationCodeGenerator.generateVerificationCode();
        try {
            emailService.sendCodeConfirmationMessage(emailToVerify, verificationCode);
        } catch (MailException ex) {
            throw new BusinessException(BusinessExceptionsEnum.CANNOT_SEND_MESSAGE);
        }

        VerificationCodeResponseDto responseDto = new VerificationCodeResponseDto();
        responseDto.setVerificationCode(verificationCode);

        return responseDto;
    }

    /**
     * Зарегистрировать организацию.
     *
     * @param signUpRequestDto Дто запроса для регистрации
     */
    @Transactional
    public void signUpOrganization(SignUpOrganizationRequestDto signUpRequestDto) {
        if (!signUpRequestDto.getPassword().equals(signUpRequestDto.getConfirmPassword())) {
            throw new BusinessException(BusinessExceptionsEnum.PASSWORD_NOT_EQUALS_CONFIRM_PASSWORD);
        }

        if (accountRepository.existsByEmail(signUpRequestDto.getOrganizationEmail())) {
            throw new BusinessException(BusinessExceptionsEnum.EMAIL_ALREADY_EXISTS);
        }

        OrganizationEntity organization = organizationMapper.signUpRequestDtoToOrganization(signUpRequestDto);

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setEmail(signUpRequestDto.getOrganizationEmail());
        accountEntity.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
        accountEntity.setRoles(Set.of(Role.ORGANIZATION));

        organizationRepository.save(organization);
        accountRepository.save(accountEntity);
    }

    /**
     * Зарегистрировать пользователя.
     *
     * @param signUpRequestDto Дто запроса для регистрации
     */
    @Transactional
    public void signUp(SignUpUserRequestDto signUpRequestDto) {
        if (!signUpRequestDto.getPassword().equals(signUpRequestDto.getConfirmPassword())) {
            throw new BusinessException(BusinessExceptionsEnum.PASSWORD_NOT_EQUALS_CONFIRM_PASSWORD);
        }

        if (accountRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new BusinessException(BusinessExceptionsEnum.EMAIL_ALREADY_EXISTS);
        }

        UserEntity user = userMapper.signUpRequestDtoToUser(signUpRequestDto);

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setEmail(signUpRequestDto.getEmail());
        accountEntity.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
        accountEntity.setRoles(Set.of(Role.USER));

        userRepository.save(user);
        accountRepository.save(accountEntity);
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        String login = loginRequestDto.getLogin();
        Validator.validateLogin(login);

        return loginByEmail(login, loginRequestDto.getPassword());
    }

    /**
     * Метод для авторизации пользователя по электронной почте.
     *
     * @param email    электронная почта
     * @param password пароль
     * @return Дто с jwt токеном
     */
    private LoginResponseDto loginByEmail(String email, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        Optional<AccountEntity> accountEntityOptional = accountRepository.findByEmailOpt(email);
        if (accountEntityOptional.isPresent()) {
            String jwtToken = jwtService.generateToken(accountEntityOptional.get());
            return LoginResponseDto.of(jwtToken);
        }

        throw new BusinessException(BusinessExceptionsEnum.USER_NOT_FOUND_BY_EMAIL);
    }

    /**
     * Получить Информацию о пользователе по токену.
     *
     * @param token Токен пользователя
     * @return Информация о пользователе по токену
     */
    public UserAuthentication getUserInfoByToken(String token) {
        try {
            return jwtUtils.getUserAuthentication(token);
        } catch (Exception e) {
            throw new BusinessException("Не удалось достать информацию из токена", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Существует ли аккаунт.
     */
    public boolean existAccount(String email) {
        return accountRepository.existsByEmail(email);
    }
}
