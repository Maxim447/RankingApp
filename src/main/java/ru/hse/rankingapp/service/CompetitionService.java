package ru.hse.rankingapp.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.rankingapp.dto.UserAuthentication;
import ru.hse.rankingapp.dto.competition.CompetitionFullInfoDto;
import ru.hse.rankingapp.dto.competition.CompetitionInfoDto;
import ru.hse.rankingapp.dto.competition.CompetitionSearchParamsDto;
import ru.hse.rankingapp.dto.competition.CreateCompetitionDto;
import ru.hse.rankingapp.dto.paging.PageRequestDto;
import ru.hse.rankingapp.dto.paging.PageResponseDto;
import ru.hse.rankingapp.entity.CompetitionEntity;
import ru.hse.rankingapp.entity.OrganizationEntity;
import ru.hse.rankingapp.enums.BusinessExceptionsEnum;
import ru.hse.rankingapp.exception.BusinessException;
import ru.hse.rankingapp.mapper.CompetitionMapper;
import ru.hse.rankingapp.repository.CompetitionRepository;
import ru.hse.rankingapp.repository.OrganizationRepository;
import ru.hse.rankingapp.service.search.CompetitionSearchWithSpec;
import ru.hse.rankingapp.utils.JwtUtils;

import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для работы с соревнованиями.
 */
@Service
@RequiredArgsConstructor
public class CompetitionService {

    private final CompetitionSearchWithSpec competitionSearchWithSpec;
    private final CompetitionRepository competitionRepository;
    private final CompetitionMapper competitionMapper;
    private final OrganizationRepository organizationRepository;
    private final JwtUtils jwtUtils;

    /**
     * Создать соревнование.
     *
     * @param createCompetitionDto Дто для создания соревнования
     */
    @Transactional
    public void createCompetition(CreateCompetitionDto createCompetitionDto) {
        UserAuthentication userInfoFromRequest = jwtUtils.getUserInfoFromRequest();

        if (userInfoFromRequest == null || !userInfoFromRequest.isOrganization()) {
            throw new BusinessException(BusinessExceptionsEnum.NOT_ENOUGH_RULES);
        }

        OrganizationEntity organizationEntity = organizationRepository.findByEmail(userInfoFromRequest.getEmail());
        CompetitionEntity competitionEntity = competitionMapper.toCompetitionEntity(organizationEntity, createCompetitionDto);

        CollectionUtils.emptyIfNull(competitionEntity.getEventEntities())
                .forEach(event -> event.setCompetition(competitionEntity));

        competitionRepository.save(competitionEntity);
    }

    /**
     * Найти соревнование по его uuid.
     *
     * @param uuid юид соревнования
     * @return Полная информация о соревновании
     */
    public CompetitionFullInfoDto getCompetitionFullInfoByUuid(UUID uuid) {
        Optional<CompetitionEntity> competitionEntity = competitionRepository.findByCompetitionUuid(uuid);

        if (competitionEntity.isPresent()) {
            return competitionMapper.mapToCompetitionFullInfo(competitionEntity.get());
        }

        throw new BusinessException("Не удалось найти соревнование по uuid = " + uuid, HttpStatus.NOT_FOUND);
    }

    /**
     * Найти соревнования по поисковым параметрам.
     *
     * @param searchParams поисковые параметры
     * @param pageRequest  пагинация
     * @return соревнования
     */
    public PageResponseDto<CompetitionInfoDto> searchCompetitions(CompetitionSearchParamsDto searchParams, PageRequestDto pageRequest) {
        Specification<CompetitionEntity> specification = competitionSearchWithSpec.searchWithSpec(searchParams);

        Page<CompetitionInfoDto> page = competitionRepository.findAll(specification, pageRequest.toPageRequest())
                .map(competitionMapper::mapToCompetitionInfoDto);

        return new PageResponseDto<>(page.getTotalElements(), page.getTotalPages(), page.getContent());
    }
}
