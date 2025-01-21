package ru.hse.rankingapp.service.auth;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.hse.rankingapp.entity.OrganizationEntity;
import ru.hse.rankingapp.entity.UserEntity;
import ru.hse.rankingapp.properties.EmailCodeConfirmationProperties;

/**
 * Класс для работы с электронной почтой.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    private final EmailCodeConfirmationProperties emailCodeConfirmationProperties;

    /**
     * Отправить код подтверждения.
     *
     * @param emailToSend электронная почта адресата
     * @param code код подтверждения
     */
    public void sendCodeConfirmationMessage(String emailToSend, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailCodeConfirmationProperties.getSenderEmail());
        message.setTo(emailToSend);
        message.setSubject(emailCodeConfirmationProperties.getSubjectText());
        message.setText(code);
        emailSender.send(message);
        log.info("Письмо с кодом подтверждения успешно отправлено. Email = {}", emailToSend);
    }

    /**
     * Отправить письмо на добавление в организацию.
     *
     * @param userEntity сущность пользователя
     */
    public void sendConfirmationMessage(UserEntity userEntity, OrganizationEntity organization) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        String userEmail = userEntity.getEmail();
        String confirmationLink = String.format(
                "http://127.0.0.1:9000/api/v1/user/confirm-invite?userEmail=%s&organizationEmail=%s",
                userEmail, organization.getEmail()
        );

        String subject = "Подтверждение вступления в организацию";
        String body = """
                <html>
                <body>
                    <p>Здравствуйте!</p>
                    <p>Пожалуйста, подтвердите вступление в организацию %s, нажав на кнопку ниже:</p>
                    <a href="%s" style="display:inline-block; padding:10px 20px; background-color:#4CAF50; color:white; text-decoration:none; border-radius:5px;">Вступить в организацию</a>
                    <p>С уважением, Ваша команда.</p>
                    <p>Если письмо пришло к Вам по ошибке, проигнорируете его.<p>
                </body>
                </html>
                """.formatted(organization.getName(), confirmationLink);

        helper.setFrom(emailCodeConfirmationProperties.getSenderEmail());
        helper.setTo(userEmail);
        helper.setSubject(subject);
        helper.setText(body, true);

        emailSender.send(mimeMessage);
        log.info("Письмо на вступление в организацию успешно отправлено. Email = {}", userEmail);
    }
}