package ru.hse.rankingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.rankingapp.dto.notification.NotificationResponse;
import ru.hse.rankingapp.service.NotificationService;

import java.util.List;

/**
 * API для уведомлений.
 */
@Tag(name = "Notification", description = "API для уведомлений")
@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Получить список уведомлений авторизованного пользователя.
     */
    @GetMapping
    @Operation(summary = "Получить список уведомлений авторизованного пользователя")
    public List<NotificationResponse> getNotificationResponseList() {
        return notificationService.getNotificationResponseList();
    }

    /**
     * Удалить уведомление по id.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить уведомление по id")
    public void deleteNotificationById(@PathVariable(value = "id") Long id) {
        notificationService.deleteById(id);
    }

    /**
     * Удалить уведомления по их id.
     */
    @DeleteMapping
    @Operation(summary = "Удалить уведомления по их id")
    public void deleteNotificationById(@RequestBody List<Long> ids) {
        notificationService.deleteAllByIds(ids);
    }
}
