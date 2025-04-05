package ru.hse.rankingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hse.rankingapp.dto.notification.NotificationResponse;
import ru.hse.rankingapp.entity.NotificationEntity;

import java.util.List;

/**
 * Репозиторий для работы с уведомлениями.
 */
@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    /**
     * Получить все уведомления по email.
     */
    @Query(value = """
            select new ru.hse.rankingapp.dto.notification.NotificationResponse(ne.id, ne.text) from NotificationEntity ne
            left join ne.accountEntity ae
            where ae.email = :email
            """)
    List<NotificationResponse> findByEmail(@Param(value = "email") String email);

    /**
     * Удалить уведомление по id.
     */
    @Modifying
    @Query(value = """
            DELETE FROM NotificationEntity ne WHERE ne.id = :id
            """)
    void deleteNotificationById(@Param(value = "id") Long id);

    /**
     * Удалить уведомления по их id.
     */
    @Modifying
    @Query(value = """
            DELETE FROM NotificationEntity ne WHERE ne.id in :ids
            """)
    void deleteAllNotificationById(@Param(value = "ids") List<Long> ids);
}
