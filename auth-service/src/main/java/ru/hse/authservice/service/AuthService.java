package ru.hse.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.authservice.dto.LoginRequestDto;
import ru.hse.authservice.dto.LoginResponseDto;
import ru.hse.authservice.dto.VerificationCodeResponseDto;
import ru.hse.authservice.dto.organization.SignUpOrganizationRequestDto;
import ru.hse.authservice.dto.user.SignUpUserRequestDto;
import ru.hse.authservice.entity.Organization;
import ru.hse.authservice.entity.User;
import ru.hse.authservice.mapper.OrganizationMapper;
import ru.hse.authservice.mapper.UserMapper;
import ru.hse.authservice.repository.OrganizationRepository;
import ru.hse.authservice.repository.UserRepository;
import ru.hse.authservice.utils.Validator;
import ru.hse.authservice.utils.VerificationCodeGenerator;
import ru.hse.commonmodule.enums.BusinessExceptionsEnum;
import ru.hse.commonmodule.exception.BusinessException;

import java.util.Optional;

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
    public void signUpOrganization(SignUpOrganizationRequestDto signUpRequestDto) {
        if (!signUpRequestDto.getPassword().equals(signUpRequestDto.getConfirmPassword())) {
            throw new BusinessException(BusinessExceptionsEnum.PASSWORD_NOT_EQUALS_CONFIRM_PASSWORD);
        }

        if (organizationRepository.existsByEmail(signUpRequestDto.getOrganizationEmail())) {
            throw new BusinessException(BusinessExceptionsEnum.EMAIL_ALREADY_EXISTS);
        }

        Organization organization = organizationMapper.signUpRequestDtoToOrganization(signUpRequestDto);
        organization.setPassword(passwordEncoder.encode(organization.getPassword()));
        organizationRepository.save(organization);
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

        if (userRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new BusinessException(BusinessExceptionsEnum.EMAIL_ALREADY_EXISTS);
        }

        User user = userMapper.signUpRequestDtoToUser(signUpRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
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

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            String jwtToken = jwtService.generateToken(userOptional.get());
            return LoginResponseDto.of(jwtToken);
        }

        Optional<Organization> organizationOptional = organizationRepository.findByEmail(email);
        if (organizationOptional.isPresent()) {
            String jwtToken = jwtService.generateToken(organizationOptional.get());
            return LoginResponseDto.of(jwtToken);
        }

        throw new BusinessException(BusinessExceptionsEnum.USER_NOT_FOUND_BY_EMAIL);
    }
}
