package ru.hse.rankingapp.service;

import lombok.RequiredArgsConstructor;
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
public class SchedulerService {

    private final CompetitionRepository competitionRepository;

    /**
     * Изменить статус соревнования и его заплывов на IN_PROGRESS.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void changeStatusInCompetition() {
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
    }
}
