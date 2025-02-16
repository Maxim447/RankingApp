package ru.hse.rankingapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.rankingapp.dto.UserAuthentication;
import ru.hse.rankingapp.dto.event.CreateEventDto;
import ru.hse.rankingapp.dto.event.EventFullInfoDto;
import ru.hse.rankingapp.entity.CompetitionEntity;
import ru.hse.rankingapp.entity.EventEntity;
import ru.hse.rankingapp.entity.EventUserLinkEntity;
import ru.hse.rankingapp.entity.UserEntity;
import ru.hse.rankingapp.entity.enums.Role;
import ru.hse.rankingapp.enums.BusinessExceptionsEnum;
import ru.hse.rankingapp.exception.BusinessException;
import ru.hse.rankingapp.mapper.EventMapper;
import ru.hse.rankingapp.repository.CompetitionRepository;
import ru.hse.rankingapp.repository.EventRepository;
import ru.hse.rankingapp.utils.JwtUtils;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для работы с мероприятиями.
 */
@Service
@RequiredArgsConstructor
public class EventService {

    private static final String XLSX_TEMPLATE_NAME = "Шаблон_для_заполнения_результатов_заплыва_%s.xlsx";

    private final EventRepository eventRepository;
    private final CompetitionRepository competitionRepository;
    private final EventMapper eventMapper;
    private final JwtUtils jwtUtils;
    private final XlsxService xlsxService;

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

        if (userInfoFromRequest.getRoles().contains(Role.USER)) {
            throw new BusinessException(BusinessExceptionsEnum.NOT_ENOUGH_RULES);
        }

        CompetitionEntity competition = competitionRepository
                .findByOrganizationEmailAndCompetitionUuid(userInfoFromRequest.getEmail(), eventDto.getCompetitionUUID())
                .orElseThrow(() -> new BusinessException("Не удалось найти соревнование", HttpStatus.NOT_FOUND));

        EventEntity event = eventMapper.toEventEntity(eventDto);
        event.addCompetition(competition);

        eventRepository.save(event);
    }

    /**
     * Получить полную информацию о заплыве.
     *
     * @param uuid uuid Заплыва
     * @return Полная информация о заплыве
     */
    public EventFullInfoDto findEventByUuid(UUID uuid) {
        Optional<EventEntity> event = eventRepository.findByUuid(uuid);

        if (event.isPresent()) {
            return eventMapper.toEventFullInfoDto(event.get());
        }

        throw new BusinessException("Не удалось найти заплыв по uuid = " + uuid, HttpStatus.NOT_FOUND);
    }

    /**
     * Добавить пользователя к заплыву.
     *
     * @param user      Сущность пользователя
     * @param eventUuid Юид заплыва
     */
    @Transactional
    public void addUserToEvent(UserEntity user, UUID eventUuid) {
        EventEntity event = eventRepository.findByUuid(eventUuid).orElseThrow(() ->
                new BusinessException("Не удалось найти заплыв по uuid = " + eventUuid, HttpStatus.NOT_FOUND));

        EventUserLinkEntity eventUserLinkEntity = new EventUserLinkEntity();
        eventUserLinkEntity.setUser(user);
        eventUserLinkEntity.setEvent(event);
        eventUserLinkEntity.setRegistrationDate(LocalDate.now());

        event.addEventUserLink(eventUserLinkEntity);

        eventRepository.save(event);
    }

    /**
     * Создать шаблон для заполнения результатов заплыва.
     *
     * @param eventUuid uuid Заплыва
     */
    public Pair<String, byte[]> generateXlsxTemplate(UUID eventUuid) {
        EventEntity event = eventRepository.findByUuid(eventUuid).orElseThrow(() ->
                new BusinessException("Не удалось найти заплыв по uuid = " + eventUuid, HttpStatus.NOT_FOUND));

        byte[] xlsxData = xlsxService.generateXlsxTemplateByEvent(event);

        return Pair.of(String.format(XLSX_TEMPLATE_NAME, LocalDate.now()), xlsxData);
    }
}
