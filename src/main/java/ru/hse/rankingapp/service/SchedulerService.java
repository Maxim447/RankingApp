package ru.hse.rankingapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.hse.rankingapp.entity.CompetitionEntity;
import ru.hse.rankingapp.entity.enums.ActionIndex;
import ru.hse.rankingapp.entity.enums.StatusEnum;
import ru.hse.rankingapp.repository.CompetitionRepository;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;

/**
 * Сервис планировщик.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {

    private final CompetitionRepository competitionRepository;

    /**
     * Изменить статус соревнования и его заплывов на IN_PROGRESS.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void changeStatusInCompetition() {
        log.info("Запустился шедуллер на обновление статуса соревнований и заплывов на IN_PROGRESS. Время: {}", OffsetDateTime.now());

        Set<CompetitionEntity> competitions = competitionRepository.findAllByCurrentDate(LocalDate.now());

        for (CompetitionEntity competition : competitions) {
            competition.setStatus(StatusEnum.IN_PROGRESS);
            competition.setModifyDttm(OffsetDateTime.now());
            competition.setActionIndex(ActionIndex.U);

            CollectionUtils.emptyIfNull(competition.getEventEntities())
                    .forEach(event -> {
                        event.setStatus(StatusEnum.IN_PROGRESS);
                        event.setModifyDttm(OffsetDateTime.now());
                        event.setActionIndex(ActionIndex.U);
                    });
        }

        competitionRepository.saveAll(competitions);
        log.info("Шедуллер закончил обновление статуса соревнований и заплывов на IN_PROGRESS. Время: {}", OffsetDateTime.now());
    }

    /**
     * Изменить статус соревнований на ENDED.
     */
    @Scheduled(cron = "30 0 0 * * *")
    public void changeStatusInCompetitionToEnded() {
        log.info("Запустился шедуллер на обновление статуса соревнований на ENDED. Время: {}", OffsetDateTime.now());

        Set<Long> competitionIds = competitionRepository.findAllLowerCurrentDate(LocalDate.now());

        log.info("Id для обновления статуса на ENDED {}", competitionIds);

        if (CollectionUtils.isNotEmpty(competitionIds)) {
            competitionRepository.updateStatus(competitionIds);
        }

        log.info("Шедуллер закончил обновление статуса соревнований на ENDED. Время: {}", OffsetDateTime.now());
    }
}
