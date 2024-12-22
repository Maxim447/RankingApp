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
import ru.hse.authservice.dto.SignUpRequestDto;
import ru.hse.authservice.dto.VerificationCodeResponseDto;
import ru.hse.authservice.entity.User;
import ru.hse.authservice.mapper.UserMapper;
import ru.hse.authservice.repository.UserRepository;
import ru.hse.authservice.utils.Validator;
import ru.hse.authservice.utils.VerificationCodeGenerator;
import ru.hse.commonmodule.enums.BusinessExceptionsEnum;
import ru.hse.commonmodule.exception.BusinessException;

/**
 * Сервис для работы с аутентификацией пользователя.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
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

    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto) {
        if (!signUpRequestDto.getPassword().equals(signUpRequestDto.getConfirmPassword())) {
            throw new BusinessException(BusinessExceptionsEnum.PASSWORD_NOT_EQUALS_CONFIRM_PASSWORD);
        }

        if (userRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new BusinessException(BusinessExceptionsEnum.EMAIL_ALREADY_EXISTS);
        }

        if (userRepository.existsByPhone(signUpRequestDto.getPhone())) {
            throw new BusinessException(BusinessExceptionsEnum.PHONE_ALREADY_EXISTS);
        }

        User user = userMapper.signUpRequestDtoToUser(signUpRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        String login = loginRequestDto.getLogin();
        Validator.validateLogin(login);

        return login.contains("@") ?
                loginByEmail(login, loginRequestDto.getPassword()) :
                loginByPhone(login, loginRequestDto.getPassword());
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

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(BusinessExceptionsEnum.USER_NOT_FOUND_BY_EMAIL));

        String jwtToken = jwtService.generateToken(user);

        return LoginResponseDto.of(jwtToken);
    }

    /**
     * Метод для авторизации пользователя по номеру телефона.
     *
     * @param phone    электронная почта
     * @param password пароль
     * @return Дто с jwt токеном
     */
    private LoginResponseDto loginByPhone(String phone, String password) {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new BusinessException(BusinessExceptionsEnum.USER_NOT_FOUND_BY_PHONE));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), password)
        );

        String jwtToken = jwtService.generateToken(user);

        return LoginResponseDto.of(jwtToken);
    }
}
