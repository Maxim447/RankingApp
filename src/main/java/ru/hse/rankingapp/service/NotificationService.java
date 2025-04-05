package ru.hse.rankingapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.rankingapp.dto.UserAuthentication;
import ru.hse.rankingapp.dto.notification.NotificationResponse;
import ru.hse.rankingapp.enums.BusinessExceptionsEnum;
import ru.hse.rankingapp.exception.BusinessException;
import ru.hse.rankingapp.repository.NotificationRepository;
import ru.hse.rankingapp.utils.JwtUtils;

import java.util.List;

/**
 * Сервис для работы с уведомлениями.
 */
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final JwtUtils jwtUtils;

    /**
     * Получить список уведомлений авторизованного пользователя.
     */
    public List<NotificationResponse> getNotificationResponseList() {
        UserAuthentication userInfoFromRequest = jwtUtils.getUserInfoFromRequest();

        if (userInfoFromRequest == null) {
            throw new BusinessException(BusinessExceptionsEnum.NOT_ENOUGH_RULES);
        }

        return notificationRepository.findByEmail(userInfoFromRequest.getEmail());
    }

    /**
     * Удалить уведомление по id.
     */
    @Transactional
    public void deleteById(Long id) {
        notificationRepository.deleteNotificationById(id);
    }

    /**
     * Удалить уведомления по их id.
     */
    @Transactional
    public void deleteAllByIds(List<Long> ids) {
        notificationRepository.deleteAllNotificationById(ids);
    }
}
