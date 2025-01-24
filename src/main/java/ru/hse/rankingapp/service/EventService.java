package ru.hse.rankingapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.hse.rankingapp.dto.UserAuthentication;
import ru.hse.rankingapp.dto.event.CreateEventDto;
import ru.hse.rankingapp.entity.CompetitionEntity;
import ru.hse.rankingapp.entity.EventEntity;
import ru.hse.rankingapp.entity.enums.Role;
import ru.hse.rankingapp.enums.BusinessExceptionsEnum;
import ru.hse.rankingapp.exception.BusinessException;
import ru.hse.rankingapp.mapper.EventMapper;
import ru.hse.rankingapp.repository.CompetitionRepository;
import ru.hse.rankingapp.repository.EventRepository;
import ru.hse.rankingapp.utils.JwtUtils;

/**
 * Сервис для работы с мероприятиями.
 */
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final CompetitionRepository competitionRepository;
    private final EventMapper eventMapper;
    private final JwtUtils jwtUtils;

    /**
     * Создать мероприятие к определенному соревнованию.
     *
     * @param eventDto дто для создания мероприятия
     */
    public void createEvent(CreateEventDto eventDto) {
        UserAuthentication userInfoFromRequest = jwtUtils.getUserInfoFromRequest();

        if (userInfoFromRequest == null) {
            throw new BusinessException("Не удалось получить информацию о пользователе", HttpStatus.NOT_FOUND);
        }

        if (userInfoFromRequest.getRole().equals(Role.USER)) {
            throw new BusinessException(BusinessExceptionsEnum.NOT_ENOUGH_RULES);
        }

        CompetitionEntity competition = competitionRepository
                .findByOrganizationEmailAndCompetitionUuid(userInfoFromRequest.getEmail(), eventDto.getCompetitionUUID())
                .orElseThrow(() -> new BusinessException("Не удалось найти соревнование", HttpStatus.NOT_FOUND));

        EventEntity event = eventMapper.toEventEntity(eventDto);
        event.addCompetition(competition);

        eventRepository.save(event);
    }
}
