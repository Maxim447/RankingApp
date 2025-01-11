package ru.hse.rankingapp.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.rankingapp.dto.competition.CreateCompetitionDto;
import ru.hse.rankingapp.entity.CompetitionEntity;
import ru.hse.rankingapp.entity.OrganizationEntity;
import ru.hse.rankingapp.enums.BusinessExceptionsEnum;
import ru.hse.rankingapp.exception.BusinessException;
import ru.hse.rankingapp.mapper.CompetitionMapper;
import ru.hse.rankingapp.repository.CompetitionRepository;

/**
 * Сервис для работы с соревнованиями.
 */
@Service
@RequiredArgsConstructor
public class CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final CompetitionMapper competitionMapper;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Создать соревнование.
     *
     * @param organization организация
     * @param createCompetitionDto  Дто для создания соревнования
     */
    @Transactional
    public void createCompetition(OrganizationEntity organization, CreateCompetitionDto createCompetitionDto) {
        if (organization == null) {
            throw new BusinessException(BusinessExceptionsEnum.NOT_ENOUGH_RULES);
        }

        OrganizationEntity attachedEntity = entityManager.find(OrganizationEntity.class, organization.getId());

        CompetitionEntity competitionEntity = competitionMapper.toCompetitionEntity(attachedEntity, createCompetitionDto);

        competitionRepository.save(competitionEntity);
    }
}
