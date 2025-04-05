package ru.hse.rankingapp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.hse.rankingapp.entity.AccountEntity;
import ru.hse.rankingapp.entity.OrganizationEntity;
import ru.hse.rankingapp.entity.TokenEntity;
import ru.hse.rankingapp.entity.UserEntity;
import ru.hse.rankingapp.enums.BusinessExceptionsEnum;
import ru.hse.rankingapp.exception.BusinessException;
import ru.hse.rankingapp.properties.EmailCodeConfirmationProperties;
import ru.hse.rankingapp.properties.EmailLinkProperties;
import ru.hse.rankingapp.utils.FioUtils;

import java.util.UUID;

/**
 * Класс для работы с электронной почтой.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    private final EmailCodeConfirmationProperties emailCodeConfirmationProperties;
    private final EmailLinkProperties emailLinkProperties;

    /**
     * Отправить код подтверждения.
     *
     * @param emailToSend электронная почта адресата
     * @param code        код подтверждения
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
     * @param tokenEntity сущность для отправки email
     */
    public void sendConfirmationMessage(TokenEntity tokenEntity) throws MessagingException {
        log.info("Страт отправки сообщения на почту для tokenUUID = {}", tokenEntity.getId());
        String userEmail = tokenEntity.getUser().getEmail();
        String confirmationLink = String.format(
                emailLinkProperties.getAddToOrganization(), tokenEntity.getId()
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
                """.formatted(tokenEntity.getOrganization().getName(), confirmationLink);

        sendMessageWithBody(subject, body, userEmail);
    }

    /**
     * Отправить письмо для восстановления пароля пользователю.
     */
    public UUID sendUserRecoveryPasswordEmail(UserEntity userEntity) {
        UUID uuid = UUID.randomUUID();

        try {
            sendRecoveryPasswordEmail(userEntity.getEmail(),
                    FioUtils.buildFullName(userEntity.getFirstName(), userEntity.getLastName(), userEntity.getMiddleName()),
                    uuid);
        } catch (Exception e) {
            throw new BusinessException(BusinessExceptionsEnum.CANNOT_SEND_MESSAGE);
        }

        return uuid;
    }

    /**
     * Отправить письмо для восстановления пароля организации.
     */
    public UUID sendOrganizationRecoveryPasswordEmail(OrganizationEntity organizationEntity) {
        UUID uuid = UUID.randomUUID();

        try {
            sendRecoveryPasswordEmail(organizationEntity.getEmail(), organizationEntity.getName(), uuid);
        } catch (Exception e) {
            throw new BusinessException(BusinessExceptionsEnum.CANNOT_SEND_MESSAGE);
        }

        return uuid;
    }

    /**
     * Отправить письмо для добавления роли куратора организации.
     */
    public void sendMessageToAddCurator(AccountEntity adminAccount, OrganizationEntity organization, String text, UUID token) throws MessagingException {
        log.info("Страт отправки сообщения на почту {} для добавления роли куратора", adminAccount.getEmail());

        String confirmationLink = String.format(
                emailLinkProperties.getAddRoleCurator(), token
        );

        String subject = "Добавление роли куратора";
        String body = """
                <html>
                <body>
                    <p>Сообщение организации:</p>
                    <p>%s</p>
                    <p>Почта организации: %s </>
                    <p>Вы можете добавить роль куратора организации "%s", нажав на кнопку ниже. Или через панель админа на сайте.</p>
                    <a href="%s" style="display:inline-block; padding:10px 20px; background-color:#4CAF50; color:white; text-decoration:none; border-radius:5px;">Добавить роль куратора</a>
                    <p>С уважением, Ваша команда.</p>
                    <p>Если письмо пришло к Вам по ошибке, проигнорируете его.<p>
                </body>
                </html>
                """.formatted(text, organization.getEmail(), organization.getName(), confirmationLink);

        sendMessageWithBody(subject, body, adminAccount.getEmail());
    }

    private void sendRecoveryPasswordEmail(String email, String name, UUID uuid) throws MessagingException {
        log.info("Страт отправки сообщения на почту для tokenUUID = {}", uuid);

        String confirmationLink = String.format(emailLinkProperties.getRecoveryPassword(), uuid);

        String subject = "Сброс пароля";

        String body = """
                <html>
                <body>
                    <p>Здравствуйте, %s!</p>
                    <p>Вы запросили сброс пароля для своей учетной записи. Чтобы изменить пароль, нажмите на кнопку ниже:</p>
                    <a href="%s" style="display:inline-block; padding:10px 20px; background-color:#4CAF50; color:white; text-decoration:none; border-radius:5px;">Изменить пароль</a>
                    <p>Если вы не запрашивали сброс пароля, проигнорируйте это письмо.<p>
                    <p>С уважением, Ваша команда.</p>
                </body>
                </html>
                """.formatted(name, confirmationLink);

        sendMessageWithBody(subject, body, email);
    }

    private void sendMessageWithBody(String subject, String htmlBody, String sendTo) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom(emailCodeConfirmationProperties.getSenderEmail());
        helper.setTo(sendTo);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        emailSender.send(mimeMessage);
        log.info("Письмо на вступление в организацию успешно отправлено. Email = {}", sendTo);
    }
}