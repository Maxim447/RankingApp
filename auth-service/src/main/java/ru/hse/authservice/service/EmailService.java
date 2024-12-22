package ru.hse.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.hse.authservice.properties.EmailCodeConfirmationProperties;

/**
 * Класс для работы с электронной почтой.
 */
@Service
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
    }
}